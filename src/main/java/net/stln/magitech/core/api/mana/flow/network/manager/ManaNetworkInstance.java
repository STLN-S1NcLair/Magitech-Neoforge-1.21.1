package net.stln.magitech.core.api.mana.flow.network.manager;

import net.minecraft.world.level.Level;
import net.stln.magitech.core.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.core.api.mana.flow.ManaVisualEffectHelper;
import net.stln.magitech.core.api.mana.flow.network.HandlerEndpoint;
import net.stln.magitech.core.api.mana.flow.network.NetworkSnapshot;
import net.stln.magitech.core.api.mana.flow.network.NetworkTree;
import net.stln.magitech.core.api.mana.flow.network.NetworkTreeHelper;
import net.stln.magitech.core.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.core.api.mana.handler.IBlockManaHandler;

import java.util.*;
import java.util.stream.Collectors;

public class ManaNetworkInstance {

    private NetworkSnapshot snapshot;
    private boolean dirty;
    private int tickCounter = 0;

    public ManaNetworkInstance(NetworkSnapshot snapshot) {
        this.snapshot = snapshot;
        this.tickCounter = new Random().nextInt(120);
    }

    public NetworkSnapshot getSnapshot() {
        return snapshot;
    }

    public void markDirty() {
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void update(NetworkSnapshot snapshot) {
        this.snapshot = snapshot;
        this.dirty = false;
    }

    public void tick(Level level) {
        tickCounter++;
        balance(level);
        // ネットワークの定期更新処理
        if (tickCounter > 120) {
            tickCounter = 0;
            this.markDirty();
        }
    }

    private void balance(Level level) {
        // ネットワーク内のマナの流れを計算して、各端点にマナを供給する処理
        Map<HandlerEndpoint, IBasicManaHandler> endpointToHandler = snapshot.endpoints().stream()
                .map(endpoint -> {
                    // ブロック位置からIManaHandlerを取得する処理
                    IBlockManaHandler handler = ManaTransferHelper.getManaContainer(level, endpoint.pos(), endpoint.direction());

                    return handler != null ? Map.entry(endpoint, handler) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (HandlerEndpoint endpoint : snapshot.endpoints()) {

            IBlockManaHandler handler = ManaTransferHelper.getManaContainer(level, endpoint.pos(), endpoint.direction());

            if (handler == null) continue;

            Set<IBasicManaHandler> inserted = ManaTransferHelper.balance(handler, new HashSet<>(endpointToHandler.values()));

            for (HandlerEndpoint target : snapshot.endpoints()) {
                IBasicManaHandler h = endpointToHandler.get(target);
                if (h != null && inserted.contains(h)) {
                    Set<NetworkTree.Edge> path =
                            NetworkTreeHelper.getWirelessPath(snapshot.networkTree(), endpoint.pos(), target.pos());
                    ManaVisualEffectHelper.spawnPathParticles(level, endpoint.pos(), target.pos(), path, this.tickCounter);
                }
            }
        }
    }
}
