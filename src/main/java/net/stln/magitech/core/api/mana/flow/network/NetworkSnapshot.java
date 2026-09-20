package net.stln.magitech.core.api.mana.flow.network;


import net.minecraft.core.BlockPos;

import java.util.Set;

/**
 * マナネットワークの端点・中継点・接続木の不変スナップショットです。
 * Immutable snapshot of a mana network's endpoints, waypoints, and connection tree.
 */
public record NetworkSnapshot(Set<HandlerEndpoint> endpoints, Set<BlockPos> waypoints, NetworkTree networkTree) {
    /**
     * スナップショットの全構成要素を比較します。
     * Compares all components of the snapshot.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NetworkSnapshot(
                Set<HandlerEndpoint> endpoints, Set<BlockPos> waypoints, NetworkTree networkTree
        ))) {
            return false;
        } else {
            return this.endpoints.equals(endpoints) &&
                    this.waypoints.equals(waypoints) &&
                    this.networkTree.equals(networkTree);
        }
    }
}
