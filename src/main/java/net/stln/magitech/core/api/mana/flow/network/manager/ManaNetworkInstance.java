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

/**
 * 単一のマナネットワークと、その更新状態を管理します。
 * Manages a single mana network and its update state.
 */
public class ManaNetworkInstance {
    private static final int REBUILD_INTERVAL_TICKS = 1200;

    private NetworkSnapshot snapshot;
    private boolean dirty;
    private int tickCounter = 0;

    /**
     * ネットワークスナップショットを保持したインスタンスを生成します。
     * Creates an instance holding a network snapshot.
     */
    public ManaNetworkInstance(NetworkSnapshot snapshot) {
        this.snapshot = snapshot;
        this.tickCounter = new Random().nextInt(REBUILD_INTERVAL_TICKS);
    }

    /**
     * 現在のネットワークスナップショットを返します。
     * Returns the current network snapshot.
     */
    public NetworkSnapshot getSnapshot() {
        return snapshot;
    }

    /**
     * ネットワークを再構築が必要な状態にします。
     * Marks the network as requiring a rebuild.
     */
    public void markDirty() {
        dirty = true;
    }

    /**
     * 再構築が必要な状態か判定します。
     * Determines whether the network requires a rebuild.
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * スナップショットを更新し、再構築フラグを解除します。
     * Updates the snapshot and clears the rebuild flag.
     */
    public void update(NetworkSnapshot snapshot) {
        this.snapshot = snapshot;
        this.dirty = false;
    }

    /**
     * ネットワークのマナ配分と定期更新を実行します。
     * Performs mana balancing and periodic network updates.
     */
    public void tick(Level level) {
        tickCounter++;
        balance(level);
        // ネットワークの定期更新処理 / Periodic network update
        if (tickCounter >= REBUILD_INTERVAL_TICKS) {
            tickCounter = 0;
            this.markDirty();
        }
    }

    private void balance(Level level) {
        // ネットワーク内のマナの流れを計算して、各端点にマナを供給する処理 / Calculate mana flow and supply each endpoint
        Map<HandlerEndpoint, IBasicManaHandler> endpointToHandler = snapshot.endpoints().stream()
                .map(endpoint -> {
                    // ブロック位置からIManaHandlerを取得する処理 / Get the IManaHandler at the block position
                    IBlockManaHandler handler = ManaTransferHelper.getManaContainer(level, endpoint.pos(), endpoint.direction());

                    return handler != null ? Map.entry(endpoint, handler) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (endpointToHandler.size() < 2) {
            return;
        }

        Set<IBasicManaHandler> availableHandlers = new HashSet<>(endpointToHandler.values());

        for (Map.Entry<HandlerEndpoint, IBasicManaHandler> source : endpointToHandler.entrySet()) {
            HandlerEndpoint endpoint = source.getKey();
            IBlockManaHandler handler = (IBlockManaHandler) source.getValue();

            Set<IBasicManaHandler> inserted = ManaTransferHelper.balance(handler, availableHandlers);
            if (inserted.isEmpty()) {
                continue;
            }

            for (Map.Entry<HandlerEndpoint, IBasicManaHandler> targetEntry : endpointToHandler.entrySet()) {
                HandlerEndpoint target = targetEntry.getKey();
                IBasicManaHandler h = targetEntry.getValue();
                if (h != null && inserted.contains(h)) {
                    Set<NetworkTree.Edge> path =
                            NetworkTreeHelper.getWirelessPath(snapshot.networkTree(), endpoint.pos(), target.pos());
                    ManaVisualEffectHelper.spawnPathParticles(level, endpoint.pos(), target.pos(), path, this.tickCounter);
                }
            }
        }
    }
}
