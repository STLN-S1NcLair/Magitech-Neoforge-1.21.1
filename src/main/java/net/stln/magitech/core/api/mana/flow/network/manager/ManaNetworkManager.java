package net.stln.magitech.core.api.mana.flow.network.manager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.saveddata.SavedData;
import net.stln.magitech.Magitech;
import net.stln.magitech.core.api.mana.flow.network.*;
import net.stln.magitech.core.api.mana.flow.network.connectable.IManaConnectable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ManaNetworkManager extends SavedData {

    private final Map<UUID, ManaNetworkInstance> networks = new HashMap<>();
    private final Map<HandlerEndpoint, UUID> endpointIndex = new HashMap<>();
    private final Map<BlockPos, UUID> waypointIndex = new HashMap<>();
    private final Deque<UUID> rebuildQueue = new ArrayDeque<>();
    private final Set<UUID> queuedRebuildIds = new HashSet<>();
    private final Map<UUID, Set<HandlerEndpoint>> rebuildPendingEndpoints = new HashMap<>();

    private static final int MAX_HOPS = 1024;
    private static final int NETWORKS_PER_TICK_BATCH = 64;
    private static final int REBUILD_SCANS_PER_TICK = 1;

    private int tickCursor = 0;

    public ManaNetworkManager() {
    }

    public void requestRebuild(ServerLevel level, BlockPos pos, boolean removal) {

        // 削除の場合はキャッシュから取得
        if (removal) {
            // 変更点がネットワークの一部であれば、そのネットワークを再構築
            Set<UUID> rebuildTargets = new HashSet<>();
            Set<Direction> directions = new HashSet<>(List.of(Direction.values()));
            directions.add(null); // 無線接続も考慮
            for (Direction dir : directions) {
                UUID endpointNetworkId = endpointIndex.get(new HandlerEndpoint(pos, dir));
                if (endpointNetworkId != null) {
                    rebuildTargets.add(endpointNetworkId);
                }
            }
            UUID waypointNetworkId = waypointIndex.get(new BlockPos(pos));
            if (waypointNetworkId != null) {
                rebuildTargets.add(waypointNetworkId);
            }

            for (UUID targetId : rebuildTargets) {
                queueRebuild(targetId);
            }
            return;
        }

        // 端点または中継点であれば接続ネットワークの更新
        // 端点チェック
        Block block = level.getBlockState(pos).getBlock();
        IManaConnectable connectable = ((IManaConnectable) (block instanceof IManaConnectable ? block : level.getBlockEntity(pos)));
        if (connectable != null) {
            Set<Direction> directions = new HashSet<>(connectable.getConnectableDirections(level.getBlockState(pos)));
            directions.add(null); // 無線接続も考慮
            Set<HandlerEndpoint> requestEndpoints = directions.stream()
                    .map(dir -> new HandlerEndpoint(pos, dir))
                    .collect(Collectors.toSet());
            boolean hasEndpoint = false; // 変更点にHandlerEndpointがあるか
            Set<UUID> touchedNetworks = new HashSet<>();
            for (HandlerEndpoint endpoint : requestEndpoints) {
                UUID id = endpointIndex.get(endpoint);
                if (id != null) {
                    ManaNetworkInstance instance = networks.get(id);
                    if (instance != null) {
                        touchedNetworks.add(id);
                        hasEndpoint = true;
                    }
                } else {
                    // 新ネットワーク構築
                    buildNewNetwork(level, pos, endpoint.direction());
                }
            }

            for (UUID touchedId : touchedNetworks) {
                ManaNetworkInstance touched = networks.get(touchedId);
                if (touched != null) {
                    touched.markDirty();
                }
            }

            // 中継点チェック(端点の場合はチェック済み)
            if (!hasEndpoint) {
                UUID id = waypointIndex.get(new BlockPos(pos));
                if (id != null) {
                    networks.get(id).markDirty();
                } else {
                    // 新ネットワーク構築
                    buildNewNetwork(level, pos, null);
                }
            }
        }
    }

    private void buildNewNetwork(ServerLevel level, BlockPos start, @Nullable Direction side) {
        NetworkSnapshot snapshot = ManaNetworkScanner.scan(level, start, side, MAX_HOPS);

        if (snapshot != null) {
            Set<ManaNetworkInstance> overlapped = networks.values().stream()
                    .filter(n -> !Collections.disjoint(n.getSnapshot().endpoints(), snapshot.endpoints()))
                    .collect(Collectors.toSet());

            if (!overlapped.isEmpty()) {
                Set<HandlerEndpoint> combinedEndpoints = new HashSet<>(snapshot.endpoints());
                Set<BlockPos> combinedWaypoints = new HashSet<>(snapshot.waypoints());

                for (ManaNetworkInstance network : overlapped) {
                    combinedEndpoints.addAll(network.getSnapshot().endpoints());
                    combinedWaypoints.addAll(network.getSnapshot().waypoints());
                    removeNetwork(network);
                }

                Magitech.LOGGER.warn("Mana network abnormal overlap detected during build at {}, merged {} endpoints and {} waypoints", start, combinedEndpoints.size(), combinedWaypoints.size());
                NetworkSnapshot newSnapshot = new NetworkSnapshot(combinedEndpoints, combinedWaypoints, snapshot.networkTree());
                putNewNetwork(newSnapshot);
            } else {
                putNewNetwork(snapshot);
            }
        }
    }

    public void tick(Level level) {
        processRebuildQueue(level);

        if (networks.isEmpty()) {
            return;
        }

        // IDとインスタンスをペアで保持し、containsValueのO(n)探索を避ける
        List<Map.Entry<UUID, ManaNetworkInstance>> snapshot = new ArrayList<>(networks.entrySet());
        int networkCount = snapshot.size();
        int batchSize = Math.min(networkCount, NETWORKS_PER_TICK_BATCH);
        int startIndex = tickCursor % networkCount;

        for (int i = 0; i < batchSize; i++) {
            Map.Entry<UUID, ManaNetworkInstance> entry = snapshot.get((startIndex + i) % networkCount);
            ManaNetworkInstance network = entry.getValue();
            if (networks.get(entry.getKey()) != network) {
                continue;
            }

            network.tick(level);
            if (network.isDirty()) {
                queueRebuild(entry.getKey());
            }
        }

        tickCursor = (startIndex + batchSize) % networkCount;
        processRebuildQueue(level);
    }

    private void queueRebuild(UUID networkId) {
        if (networkId != null && networks.containsKey(networkId) && queuedRebuildIds.add(networkId)) {
            rebuildQueue.addLast(networkId);
        }
    }

    private void processRebuildQueue(Level level) {
        int budget = REBUILD_SCANS_PER_TICK;
        while (budget > 0 && !rebuildQueue.isEmpty()) {
            UUID networkId = rebuildQueue.pollFirst();
            if (networkId == null) {
                continue;
            }

            Set<HandlerEndpoint> remaining = rebuildPendingEndpoints.get(networkId);
            if (remaining == null) {
                ManaNetworkInstance current = networks.get(networkId);
                if (current == null) {
                    queuedRebuildIds.remove(networkId);
                    continue;
                }

                remaining = new HashSet<>(current.getSnapshot().endpoints());
                rebuildPendingEndpoints.put(networkId, remaining);
                removeNetwork(networkId, current);
            }

            if (remaining.isEmpty()) {
                finishQueuedRebuild(networkId);
                continue;
            }

            HandlerEndpoint start = remaining.iterator().next();
            NetworkSnapshot snapshot = ManaNetworkScanner.scan(level, start.pos(), start.direction(), MAX_HOPS);
            if (snapshot != null) {
                putNewNetwork(snapshot);
                remaining.removeAll(snapshot.endpoints());
            }
            remaining.remove(start);
            budget--;

            if (remaining.isEmpty()) {
                finishQueuedRebuild(networkId);
            } else {
                rebuildQueue.addLast(networkId);
            }
        }
    }

    private void finishQueuedRebuild(UUID networkId) {
        queuedRebuildIds.remove(networkId);
        rebuildPendingEndpoints.remove(networkId);
    }

    // ネットワーク削除
    private void removeNetwork(ManaNetworkInstance networkInstance) {
        if (networkInstance != null) {
            UUID keyToRemove = null;
            for (Map.Entry<UUID, ManaNetworkInstance> entry : networks.entrySet()) {
                if (entry.getValue() == networkInstance) {
                    keyToRemove = entry.getKey();
                    break;
                }
            }

            if (keyToRemove != null) {
                removeNetwork(keyToRemove, networkInstance);
            }
        }
    }

    private void removeNetwork(UUID networkId, ManaNetworkInstance networkInstance) {
        if (networkId == null || networkInstance == null) {
            return;
        }

        for (HandlerEndpoint p : networkInstance.getSnapshot().endpoints()) {
            endpointIndex.remove(p);
        }
        for (BlockPos p : networkInstance.getSnapshot().waypoints()) {
            waypointIndex.remove(new BlockPos(p));
        }
        networks.remove(networkId);
        queuedRebuildIds.remove(networkId);
        rebuildPendingEndpoints.remove(networkId);
    }

    // 新ネットワーク登録
    private UUID putNewNetwork(NetworkSnapshot snapshot) {
        UUID uuid = UUID.randomUUID();
        networks.put(uuid, new ManaNetworkInstance(snapshot));
        for (HandlerEndpoint p : snapshot.endpoints()) {
            endpointIndex.put(p, uuid);
        }
        for (BlockPos p : snapshot.waypoints()) {
            waypointIndex.put(new BlockPos(p), uuid);
        }
        return uuid;
    }

    public Set<HandlerEndpoint> getEndpointsInNetwork(UUID networkId) {
        ManaNetworkInstance instance = networks.get(networkId);
        if (instance != null) {
            return instance.getSnapshot().endpoints();
        }
        return Collections.emptySet();
    }

    public UUID getNetworkIdForEndpoint(HandlerEndpoint endpoint) {
        return endpointIndex.get(endpoint);
    }

    public UUID getNetworkIdForWaypoint(BlockPos waypoint) {
        return waypointIndex.get(waypoint);
    }

    public Set<NetworkTree.Edge> getWirelessPath(UUID networkId, BlockPos start, BlockPos end) {
        ManaNetworkInstance instance = networks.get(networkId);
        if (instance != null) {
            return NetworkTreeHelper.getWirelessPath(instance.getSnapshot().networkTree(), start, end);
        }
        return Collections.emptySet();
    }

    private static final Factory<ManaNetworkManager> FACTORY = new Factory<>(ManaNetworkManager::new, ((compoundTag, provider) -> new ManaNetworkManager()), null);

    public static ManaNetworkManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, "magitech_mana_network");
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        return tag;
    }
}
