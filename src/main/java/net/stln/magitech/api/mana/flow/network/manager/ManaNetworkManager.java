package net.stln.magitech.api.mana.flow.network.manager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.mana.flow.network.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ManaNetworkManager extends SavedData {

    private final Map<UUID, ManaNetworkInstance> networks = new HashMap<>();
    private final Map<HandlerEndpoint, UUID> endpointIndex = new HashMap<>();
    private final Map<BlockPos, UUID> waypointIndex = new HashMap<>();

    private static final int MAX_HOPS = 1024;

    public ManaNetworkManager() {}

    public void requestRebuild(ServerLevel level, BlockPos pos) {

        // 端点または中継点であれば接続ネットワークの更新
        // 端点チェック
        Set<Direction> directions = new HashSet<>(List.of(Direction.values()));
        directions.add(null); // nullは内部アクセス/無線アクセスを意味する
        Set<HandlerEndpoint> requestEndpoints = directions.stream()
                .map(dir -> new HandlerEndpoint(pos, dir))
                .collect(Collectors.toSet());
        boolean hasEndpoint = false; // 変更点にHandlerEndpointがあるか
        for (HandlerEndpoint endpoint : requestEndpoints) {
            UUID id = endpointIndex.get(endpoint);
            if (id != null) {
                networks.get(id).markDirty();
                hasEndpoint = true;
            } else {
                // 新ネットワーク構築
                buildNewNetwork(level, pos, endpoint.direction());
            }
        }

        // 中継点チェック(端点の場合はチェック済み)
        if (!hasEndpoint) {
            UUID id = waypointIndex.get(pos);
             if (id != null) {
                 networks.get(id).markDirty();
             } else {
                 // 新ネットワーク構築
                 buildNewNetwork(level, pos, null);
             }
        } else {
            buildNewNetwork(level, pos, null);
        }
    }

    private void buildNewNetwork(ServerLevel level, BlockPos start, @Nullable Direction side) {
        NetworkSnapshot snapshot = ManaNetworkScanner.scan(level, start, side, MAX_HOPS);

        Set<ManaNetworkInstance> overlapped = networks.values().stream()
                .filter(n -> !Collections.disjoint(n.getSnapshot().endpoints(), snapshot.endpoints()))
                .collect(Collectors.toSet());

        Set<HandlerEndpoint> combinedEndpoints = new HashSet<>(snapshot.endpoints());
        Set<BlockPos> combinedWaypoints = new HashSet<>(snapshot.waypoints());
        for (ManaNetworkInstance network : overlapped) {
            // 古いposToNetwork削除
            // ネットワーク統合
            boolean changed = combinedEndpoints.addAll(network.getSnapshot().endpoints());
            changed |= combinedWaypoints.addAll(network.getSnapshot().waypoints());
            if (changed) {
                // ネットワークが変化した場合は異常: 通知
                Magitech.LOGGER.warn("Mana network abnormal overlap detected during build at {}, merged {} endpoints and {} waypoints", start, combinedEndpoints.size(), combinedWaypoints.size());
            }
            removeNetwork(network);
        }

        NetworkSnapshot newSnapshot = new NetworkSnapshot(combinedEndpoints, combinedWaypoints, snapshot.networkTree());
        putNewNetwork(newSnapshot);
    }

    public void tick(Level level) {
        for (ManaNetworkInstance network : networks.values()) {
            // ネットワークの定期更新処理
            network.tick(level);
            if (network.isDirty()) {
                rebuild(network, level);
            }
        }
    }

    private void rebuild(ManaNetworkInstance network, Level level) {
        Set<HandlerEndpoint> unconnectedEndpoints = network.getSnapshot().endpoints();
        while (!unconnectedEndpoints.isEmpty()) {
            HandlerEndpoint start = unconnectedEndpoints.iterator().next();
            NetworkSnapshot snapshot = ManaNetworkScanner.scan(level, start.pos(), start.direction(), MAX_HOPS);

            // 古いposToNetwork削除
            // 新snapshot再登録

            putNewNetwork(snapshot);

            unconnectedEndpoints.removeAll(snapshot.endpoints());
        }
        removeNetwork(network);
    }

    // ネットワーク削除
    private void removeNetwork(ManaNetworkInstance networkInstance) {
        if (networkInstance != null && networks.containsValue(networkInstance)) {
            for (HandlerEndpoint p : networkInstance.getSnapshot().endpoints()) {
                endpointIndex.remove(p);
            }
            for (BlockPos p : networkInstance.getSnapshot().waypoints()) {
                waypointIndex.remove(p);
            }
            networks.values().remove(networkInstance);
        }
    }

    // 新ネットワーク登録
    private UUID putNewNetwork(NetworkSnapshot snapshot) {
        UUID uuid = UUID.randomUUID();
        networks.put(uuid, new ManaNetworkInstance(snapshot));

        for (HandlerEndpoint p : snapshot.endpoints()) {
            endpointIndex.put(p, uuid);
        }
        for (BlockPos p : snapshot.waypoints()) {
            waypointIndex.put(p, uuid);
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
