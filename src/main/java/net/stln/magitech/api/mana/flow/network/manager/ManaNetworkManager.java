package net.stln.magitech.api.mana.flow.network.manager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.mana.flow.network.HandlerEndpoint;
import net.stln.magitech.api.mana.flow.network.ManaNetworkScanner;
import net.stln.magitech.api.mana.flow.network.NetworkSnapshot;

import java.util.*;
import java.util.stream.Collectors;

public class ManaNetworkManager extends SavedData {

    private final Map<UUID, ManaNetworkInstance> networks = new HashMap<>();
    private final Map<HandlerEndpoint, UUID> endpointIndex = new HashMap<>();

    private static final int MAX_HOPS = 1024;

    public ManaNetworkManager() {}

    public void requestRebuild(ServerLevel level, BlockPos pos) {
        UUID id = endpointIndex.get(pos);
        if (id != null) {
            networks.get(id).markDirty();
        } else {
            buildNewNetwork(level, pos);
        }
    }

    private void buildNewNetwork(ServerLevel level, BlockPos start) {
        NetworkSnapshot snapshot = ManaNetworkScanner.scan(level, start, MAX_HOPS);

        Set<ManaNetworkInstance> overlapped = networks.values().stream()
                .filter(n -> !Collections.disjoint(n.getSnapshot().endpoints(), snapshot.endpoints()))
                .collect(Collectors.toSet());

        Set<HandlerEndpoint> combinedEndpoints = new HashSet<>(snapshot.endpoints());
        Set<BlockPos> combinedWaypoints = new HashSet<>(snapshot.waypoints());
        for (ManaNetworkInstance network : overlapped) {
            // 古いposToNetwork削除
            for (HandlerEndpoint p : network.getSnapshot().endpoints()) {
                endpointIndex.remove(p);
            }
            // ネットワーク統合
            boolean changed = combinedEndpoints.addAll(network.getSnapshot().endpoints());
            changed |= combinedWaypoints.addAll(network.getSnapshot().waypoints());
            if (changed) {
                // ネットワークが変化した場合は異常: 通知
                Magitech.LOGGER.warn("Mana network overlap detected during build at {}, merged {} endpoints and {} waypoints", start, combinedEndpoints.size(), combinedWaypoints.size());
            }
            networks.remove(network);
        }

        NetworkSnapshot newSnapshot = new NetworkSnapshot(combinedEndpoints, combinedWaypoints);
        UUID uuid = UUID.randomUUID();

        networks.put(uuid, new ManaNetworkInstance(newSnapshot));

        for (HandlerEndpoint p : newSnapshot.endpoints()) {
            endpointIndex.put(p, uuid);
        }
    }

    public void tick(Level level) {
        for (ManaNetworkInstance network : networks.values()) {
            if (network.isDirty()) {
                rebuild(network, level);
            }
        }
    }

    private void rebuild(ManaNetworkInstance network, Level level) {
        NetworkSnapshot snapshot = ManaNetworkScanner.scan(...);

        // 古いposToNetwork削除
        // 新snapshot再登録

        network.update(snapshot);
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
