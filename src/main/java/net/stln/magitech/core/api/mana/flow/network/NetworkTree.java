package net.stln.magitech.core.api.mana.flow.network;


import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class NetworkTree {
    private final Set<BlockPos> vertices;
    private final Set<Edge> edges;
    private final Map<BlockPos, BlockPos> parentByChild;
    private final Map<BlockPos, Edge> edgeByChild;

    public NetworkTree(Set<BlockPos> vertices, Set<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.parentByChild = new HashMap<>();
        this.edgeByChild = new HashMap<>();
        indexEdges(edges);
    }

    public NetworkTree(Set<HandlerEndpoint> endpoints, Set<BlockPos> waypoints, Set<Edge> edges) {
        this.edges = edges;
        this.vertices = new HashSet<>();
        this.parentByChild = new HashMap<>();
        this.edgeByChild = new HashMap<>();
        for (HandlerEndpoint endpoint : endpoints) {
            this.vertices.add(endpoint.pos());
        }
        this.vertices.addAll(waypoints);
        indexEdges(edges);
    }

    private void indexEdges(Set<Edge> edges) {
        for (Edge edge : edges) {
            parentByChild.put(edge.child(), edge.parent());
            edgeByChild.put(edge.child(), edge);
        }
    }

    public Set<BlockPos> getVertices() {
        return vertices;
    }

    public BlockPos getParent(BlockPos vertex) {
        return parentByChild.get(vertex);
    }

    public Edge getEdge(BlockPos parent, BlockPos child) {
        Edge edge = edgeByChild.get(child);
        if (edge != null && edge.parent().equals(parent)) {
            return edge;
        }

        edge = edgeByChild.get(parent);
        if (edge != null && edge.parent().equals(child)) {
            return edge;
        }
        return null; // 見つからない場合はnullを返す
    }

    public Edge getEdge(BlockPos child) {
        return edgeByChild.get(child); // 見つからない場合はnullを返す
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
