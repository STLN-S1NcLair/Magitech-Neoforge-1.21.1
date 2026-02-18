package net.stln.magitech.api.mana.flow.network;


import net.minecraft.core.BlockPos;

import java.util.*;

public class NetworkTree {
    private final Set<BlockPos> vertices;
    private final Set<Edge> edges;

    public NetworkTree(Set<BlockPos> vertices, Set<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public NetworkTree(Set<HandlerEndpoint> endpoints, Set<BlockPos> waypoints, Set<Edge> edges) {
        this.edges = edges;
        this.vertices = new HashSet<>();
        for (HandlerEndpoint endpoint : endpoints) {
            this.vertices.add(endpoint.pos());
        }
        this.vertices.addAll(waypoints);
    }

    public Set<BlockPos> getVertices() {
        return vertices;
    }

    public BlockPos getParent(BlockPos vertex) {
        return edges.stream()
                .filter(edge -> edge.child().equals(vertex))
                .map(Edge::parent)
                .findFirst()
                .orElse(null); // 見つからない場合はnullを返す
    }

    public Edge getEdge(BlockPos parent, BlockPos child) {
        for (Edge edge : edges) {
            if ((edge.parent().equals(parent) && edge.child().equals(child)) ||
                (edge.parent().equals(child) && edge.child().equals(parent))) {
                return edge;
            }
        }
        return null; // 見つからない場合はnullを返す
    }

    public Edge getEdge(BlockPos child) {
        for (Edge edge : edges) {
            if (edge.child.equals(child)) {
                return edge;
            }
        }
        return null; // 見つからない場合はnullを返す
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkTree that)) return false;
        return Objects.equals(vertices, that.vertices) && Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, edges);
    }

    // 有向辺
    public record Edge(BlockPos parent, BlockPos child, ConnectionMode mode) {

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Edge(BlockPos f, BlockPos t, ConnectionMode m))) return false;
            return (this.parent.equals(f) && this.child.equals(t)
                    || this.parent.equals(t) && this.child.equals(f))
                    && this.mode == m;
        }

        public Edge reverse() {
            return new Edge(this.child, this.parent, this.mode);
        }
    }
}
