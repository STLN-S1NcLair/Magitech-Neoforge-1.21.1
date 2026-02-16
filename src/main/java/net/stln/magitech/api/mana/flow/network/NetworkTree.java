package net.stln.magitech.api.mana.flow.network;


import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NetworkTree {
    private final Set<BlockPos> edges;
    private final Map<BlockPos, BlockPos> parent;

    public NetworkTree(Set<BlockPos> edges, Map<BlockPos, BlockPos> parent) {
        this.edges = edges;
        this.parent = parent;
    }

    public NetworkTree(Set<HandlerEndpoint> endpoints, Set<BlockPos> waypoints, Map<BlockPos, BlockPos> parent) {
        this.edges = new HashSet<>();
        for (HandlerEndpoint endpoint : endpoints) {
            this.edges.add(endpoint.pos());
        }
        this.edges.addAll(waypoints);
        this.parent = parent;
    }

    public Set<BlockPos> getEdges() {
        return edges;
    }

    public Map<BlockPos, BlockPos> getParent() {
        return parent;
    }
}
