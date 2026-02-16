package net.stln.magitech.api.mana.flow.network;


import net.minecraft.core.BlockPos;

import java.util.Set;

public record NetworkSnapshot(Set<HandlerEndpoint> endpoints, Set<BlockPos> waypoints, Set<WirelessEdge> wirelessPaths) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NetworkSnapshot(Set<HandlerEndpoint> endpoints, Set<BlockPos> waypoints, Set<WirelessEdge> wirelessPaths))) {
            return false;
        } else {
            return this.endpoints.equals(endpoints) &&
                    this.waypoints.equals(waypoints) &&
                    this.wirelessPaths.equals(wirelessPaths);
        }
    }

    public record WirelessEdge(BlockPos from, BlockPos to) {
    }

    public record WirelessPath(BlockPos from, BlockPos to, Set<WirelessEdge> edges) {
    }
}
