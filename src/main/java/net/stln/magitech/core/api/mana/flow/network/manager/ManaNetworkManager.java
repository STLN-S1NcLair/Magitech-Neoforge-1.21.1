package net.stln.magitech.core.api.mana.flow.network.manager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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

/**
 * ワールド内のマナネットワークを保存・更新・再構築します。
 * Persists, updates, and rebuilds mana networks within a world.
 */
public class ManaNetworkManager extends SavedData {

    private static final String TAG_NETWORKS = "Networks";
    private static final String TAG_ID = "Id";
    private static final String TAG_SNAPSHOT = "Snapshot";
    private static final String TAG_ENDPOINTS = "Endpoints";
    private static final String TAG_WAYPOINTS = "Waypoints";
    private static final String TAG_EDGES = "Edges";
    private static final String TAG_POS = "Pos";
    private static final String TAG_DIR = "Dir";
    private static final String TAG_PARENT = "Parent";
    private static final String TAG_CHILD = "Child";
    private static final String TAG_MODE = "Mode";

    private final Map<UUID, ManaNetworkInstance> networks = new HashMap<>();
    private final Map<HandlerEndpoint, UUID> endpointIndex = new HashMap<>();
    private final Map<BlockPos, UUID> waypointIndex = new HashMap<>();
    private final Deque<UUID> rebuildQueue = new ArrayDeque<>();
    private final Set<UUID> queuedRebuildIds = new HashSet<>();
    private final Map<UUID, Set<HandlerEndpoint>> rebuildPendingEndpoints = new HashMap<>();

    private static final int MAX_HOPS = 1024;
    private static final int REBUILD_SCANS_PER_TICK = 1;

    /**
     * 空のマナネットワーク管理データを生成します。
     * Creates an empty mana-network manager.
     */
    public ManaNetworkManager() {
    }

    private static ManaNetworkManager load(CompoundTag tag) {
        ManaNetworkManager manager = new ManaNetworkManager();
        ListTag networksTag = tag.getList(TAG_NETWORKS, Tag.TAG_COMPOUND);
        for (Tag networkTag : networksTag) {
            if (!(networkTag instanceof CompoundTag networkCompound)) {
                continue;
            }
            if (!networkCompound.hasUUID(TAG_ID) || !networkCompound.contains(TAG_SNAPSHOT, Tag.TAG_COMPOUND)) {
                continue;
            }

            UUID networkId = networkCompound.getUUID(TAG_ID);
            NetworkSnapshot snapshot = readSnapshot(networkCompound.getCompound(TAG_SNAPSHOT));
            if (snapshot != null) {
                manager.networks.put(networkId, new ManaNetworkInstance(snapshot));
            }
        }

        manager.rebuildIndexes();
        return manager;
    }

    /**
     * 指定位置に関係するネットワークの再構築を要求します。
     * Requests rebuilding networks affected by a position.
     */
    public void requestRebuild(ServerLevel level, BlockPos pos, boolean removal) {

        // 削除の場合はキャッシュから取得 / For removals, obtain affected networks from the indexes
        if (removal) {
            // 変更点がネットワークの一部であれば、そのネットワークを再構築 / Rebuild networks that contain the changed position
            Set<UUID> rebuildTargets = new HashSet<>();
            Set<Direction> directions = new HashSet<>(List.of(Direction.values()));
            directions.add(null); // 無線接続も考慮 / Also consider wireless connections
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

        // 端点または中継点であれば接続ネットワークの更新 / Update the connected network when this is an endpoint or waypoint
        // 端点チェック / Check for an endpoint
        Block block = level.getBlockState(pos).getBlock();
        IManaConnectable connectable = ((IManaConnectable) (block instanceof IManaConnectable ? block : level.getBlockEntity(pos)));
        if (connectable != null) {
            Set<Direction> directions = new HashSet<>(connectable.getConnectableDirections(level.getBlockState(pos)));
            directions.add(null); // 無線接続も考慮 / Also consider wireless connections
            Set<HandlerEndpoint> requestEndpoints = directions.stream()
                    .map(dir -> new HandlerEndpoint(pos, dir))
                    .collect(Collectors.toSet());
            boolean hasEndpoint = false; // 変更点にHandlerEndpointがあるか / Whether the changed position has a HandlerEndpoint
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
                    // 新ネットワーク構築 / Build a new network
                    buildNewNetwork(level, pos, endpoint.direction());
                }
            }

            for (UUID touchedId : touchedNetworks) {
                ManaNetworkInstance touched = networks.get(touchedId);
                if (touched != null) {
                    touched.markDirty();
                }
            }

            // 中継点チェック(端点の場合はチェック済み) / Check for a waypoint (already checked when it is an endpoint)
            if (!hasEndpoint) {
                UUID id = waypointIndex.get(new BlockPos(pos));
                if (id != null) {
                    networks.get(id).markDirty();
                } else {
                    // 新ネットワーク構築 / Build a new network
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

    /**
     * 管理中のネットワークを tick 更新します。
     * Ticks all managed networks.
     */
    public void tick(Level level) {
        processRebuildQueue(level);

        if (networks.isEmpty()) {
            return;
        }

        // IDとインスタンスをペアで保持し、更新中のネットワーク変更に影響されないようにする
        // Keep IDs paired with instances so network changes during the update do not affect iteration
        List<Map.Entry<UUID, ManaNetworkInstance>> snapshot = new ArrayList<>(networks.entrySet());

        for (Map.Entry<UUID, ManaNetworkInstance> entry : snapshot) {
            ManaNetworkInstance network = entry.getValue();
            if (networks.get(entry.getKey()) != network) {
                continue;
            }

            network.tick(level);
            if (network.isDirty()) {
                queueRebuild(entry.getKey());
            }
        }

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

    private void rebuildIndexes() {
        endpointIndex.clear();
        waypointIndex.clear();
        rebuildQueue.clear();
        queuedRebuildIds.clear();
        rebuildPendingEndpoints.clear();

        for (Map.Entry<UUID, ManaNetworkInstance> entry : networks.entrySet()) {
            UUID id = entry.getKey();
            NetworkSnapshot snapshot = entry.getValue().getSnapshot();
            for (HandlerEndpoint endpoint : snapshot.endpoints()) {
                endpointIndex.put(endpoint, id);
            }
            for (BlockPos waypoint : snapshot.waypoints()) {
                waypointIndex.put(new BlockPos(waypoint), id);
            }
        }
    }

    // ネットワーク削除 / Remove a network
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
        setDirty();
    }

    // 新ネットワーク登録 / Register a new network
    private UUID putNewNetwork(NetworkSnapshot snapshot) {
        UUID uuid = UUID.randomUUID();
        networks.put(uuid, new ManaNetworkInstance(snapshot));
        for (HandlerEndpoint p : snapshot.endpoints()) {
            endpointIndex.put(p, uuid);
        }
        for (BlockPos p : snapshot.waypoints()) {
            waypointIndex.put(new BlockPos(p), uuid);
        }
        setDirty();
        return uuid;
    }

    /**
     * ネットワーク内の端点集合を返します。
     * Returns the endpoints in a network.
     */
    public Set<HandlerEndpoint> getEndpointsInNetwork(UUID networkId) {
        ManaNetworkInstance instance = networks.get(networkId);
        if (instance != null) {
            return instance.getSnapshot().endpoints();
        }
        return Collections.emptySet();
    }

    /**
     * 端点が所属するネットワークIDを返します。
     * Returns the network ID containing an endpoint.
     */
    public UUID getNetworkIdForEndpoint(HandlerEndpoint endpoint) {
        return endpointIndex.get(endpoint);
    }

    /**
     * 中継点が所属するネットワークIDを返します。
     * Returns the network ID containing a waypoint.
     */
    public UUID getNetworkIdForWaypoint(BlockPos waypoint) {
        return waypointIndex.get(waypoint);
    }

    /**
     * ネットワーク内の2位置間にある無線経路を返します。
     * Returns the wireless path between two positions in a network.
     */
    public Set<NetworkTree.Edge> getWirelessPath(UUID networkId, BlockPos start, BlockPos end) {
        ManaNetworkInstance instance = networks.get(networkId);
        if (instance != null) {
            return NetworkTreeHelper.getWirelessPath(instance.getSnapshot().networkTree(), start, end);
        }
        return Collections.emptySet();
    }

    private static final Factory<ManaNetworkManager> FACTORY = new Factory<>(ManaNetworkManager::new, ((compoundTag, provider) -> ManaNetworkManager.load(compoundTag)), null);

    /**
     * ワールドのマナネットワーク管理データを取得します。
     * Gets the mana-network manager data for a world.
     */
    public static ManaNetworkManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, "magitech_mana_network");
    }

    /**
     * マナネットワークをNBTへ保存します。
     * Saves mana networks to NBT.
     */
    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag networksTag = new ListTag();
        for (Map.Entry<UUID, ManaNetworkInstance> entry : networks.entrySet()) {
            CompoundTag networkTag = new CompoundTag();
            networkTag.putUUID(TAG_ID, entry.getKey());
            networkTag.put(TAG_SNAPSHOT, writeSnapshot(entry.getValue().getSnapshot()));
            networksTag.add(networkTag);
        }
        tag.put(TAG_NETWORKS, networksTag);
        return tag;
    }

    private static CompoundTag writeSnapshot(NetworkSnapshot snapshot) {
        CompoundTag snapshotTag = new CompoundTag();

        ListTag endpointsTag = new ListTag();
        for (HandlerEndpoint endpoint : snapshot.endpoints()) {
            CompoundTag endpointTag = new CompoundTag();
            endpointTag.putLong(TAG_POS, endpoint.pos().asLong());
            if (endpoint.direction() != null) {
                endpointTag.putString(TAG_DIR, endpoint.direction().getSerializedName());
            }
            endpointsTag.add(endpointTag);
        }
        snapshotTag.put(TAG_ENDPOINTS, endpointsTag);

        ListTag waypointsTag = new ListTag();
        for (BlockPos waypoint : snapshot.waypoints()) {
            CompoundTag waypointTag = new CompoundTag();
            waypointTag.putLong(TAG_POS, waypoint.asLong());
            waypointsTag.add(waypointTag);
        }
        snapshotTag.put(TAG_WAYPOINTS, waypointsTag);

        ListTag edgesTag = new ListTag();
        for (NetworkTree.Edge edge : snapshot.networkTree().getEdges()) {
            CompoundTag edgeTag = new CompoundTag();
            edgeTag.putLong(TAG_PARENT, edge.parent().asLong());
            edgeTag.putLong(TAG_CHILD, edge.child().asLong());
            edgeTag.putString(TAG_MODE, edge.mode().name());
            edgesTag.add(edgeTag);
        }
        snapshotTag.put(TAG_EDGES, edgesTag);

        return snapshotTag;
    }

    private static @Nullable NetworkSnapshot readSnapshot(CompoundTag snapshotTag) {
        Set<HandlerEndpoint> endpoints = new HashSet<>();
        for (Tag endpointRaw : snapshotTag.getList(TAG_ENDPOINTS, Tag.TAG_COMPOUND)) {
            if (!(endpointRaw instanceof CompoundTag endpointTag) || !endpointTag.contains(TAG_POS, Tag.TAG_LONG)) {
                continue;
            }

            BlockPos pos = BlockPos.of(endpointTag.getLong(TAG_POS));
            Direction direction = null;
            if (endpointTag.contains(TAG_DIR, Tag.TAG_STRING)) {
                direction = Direction.byName(endpointTag.getString(TAG_DIR));
            }
            endpoints.add(new HandlerEndpoint(pos, direction));
        }

        if (endpoints.isEmpty()) {
            return null;
        }

        Set<BlockPos> waypoints = new HashSet<>();
        for (Tag waypointRaw : snapshotTag.getList(TAG_WAYPOINTS, Tag.TAG_COMPOUND)) {
            if (waypointRaw instanceof CompoundTag waypointTag && waypointTag.contains(TAG_POS, Tag.TAG_LONG)) {
                waypoints.add(BlockPos.of(waypointTag.getLong(TAG_POS)));
            }
        }

        Set<NetworkTree.Edge> edges = new HashSet<>();
        for (Tag edgeRaw : snapshotTag.getList(TAG_EDGES, Tag.TAG_COMPOUND)) {
            if (!(edgeRaw instanceof CompoundTag edgeTag)
                    || !edgeTag.contains(TAG_PARENT, Tag.TAG_LONG)
                    || !edgeTag.contains(TAG_CHILD, Tag.TAG_LONG)
                    || !edgeTag.contains(TAG_MODE, Tag.TAG_STRING)) {
                continue;
            }

            try {
                ConnectionMode mode = ConnectionMode.valueOf(edgeTag.getString(TAG_MODE));
                edges.add(new NetworkTree.Edge(
                        BlockPos.of(edgeTag.getLong(TAG_PARENT)),
                        BlockPos.of(edgeTag.getLong(TAG_CHILD)),
                        mode
                ));
            } catch (IllegalArgumentException ignored) {
                // 不正なmodeはスキップ / Skip invalid modes
            }
        }

        return new NetworkSnapshot(endpoints, waypoints, new NetworkTree(endpoints, waypoints, edges));
    }
}
