package net.stln.magitech.api.mana.flow.network;


import net.minecraft.core.BlockPos;

import java.util.Set;

public record NetworkSnapshot(Set<HandlerEndpoint> endpoints, Set<BlockPos> waypoints) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NetworkSnapshot(Set<HandlerEndpoint> endpoints, Set<BlockPos> waypoints))) {
            return false;
        } else {
            return this.endpoints.equals(endpoints) &&
                    this.waypoints.equals(waypoints);
        }
    }
}
