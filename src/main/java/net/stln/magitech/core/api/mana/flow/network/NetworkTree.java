package net.stln.magitech.core.api.mana.flow.network;


import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * マナネットワークのノードと有向接続辺を保持します。
 * Stores the nodes and directed connection edges of a mana network.
 */
public class NetworkTree {
    private final Set<BlockPos> vertices;
    private final Set<Edge> edges;

    /**
     * 頂点集合だけでネットワーク木を生成します。
     * Creates a network tree from a vertex and edge set.
     */
    public NetworkTree(Set<BlockPos> vertices, Set<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    /**
     * 端点・中継点からネットワーク木を生成します。
     * Creates a network tree from endpoints and waypoints.
     */
    public NetworkTree(Set<HandlerEndpoint> endpoints, Set<BlockPos> waypoints, Set<Edge> edges) {
        this.edges = edges;
        this.vertices = new HashSet<>();
        for (HandlerEndpoint endpoint : endpoints) {
            this.vertices.add(endpoint.pos());
        }
        this.vertices.addAll(waypoints);
    }

    /**
     * ネットワークの頂点集合を返します。
     * Returns the network's vertex set.
     */
    public Set<BlockPos> getVertices() {
        return vertices;
    }

    /**
     * ネットワークの有向辺集合を返します。
     * Returns the network's directed edge set.
     */
    public Set<Edge> getEdges() {
        return edges;
    }

    /**
     * 指定頂点の親頂点を返します。
     * Returns the parent vertex of a given vertex.
     */
    public BlockPos getParent(BlockPos vertex) {
        return edges.stream()
                .filter(edge -> edge.child().equals(vertex))
                .map(Edge::parent)
                .findFirst()
                .orElse(null); // 見つからない場合はnullを返す / Return null when none is found
    }

    /**
     * 指定した親子位置を結ぶ辺を返します。
     * Returns the edge connecting the specified parent and child positions.
     */
    public Edge getEdge(BlockPos parent, BlockPos child) {
        for (Edge edge : edges) {
            if ((edge.parent().equals(parent) && edge.child().equals(child)) ||
                    (edge.parent().equals(child) && edge.child().equals(parent))) {
                return edge;
            }
        }
        return null; // 見つからない場合はnullを返す / Return null when none is found
    }

    /**
     * 指定子頂点へ入る辺を返します。
     * Returns the edge entering the specified child vertex.
     */
    public Edge getEdge(BlockPos child) {
        for (Edge edge : edges) {
            if (edge.child.equals(child)) {
                return edge;
            }
        }
        return null; // 見つからない場合はnullを返す / Return null when none is found
    }

    /**
     * 頂点集合と辺集合に基づいて等価性を判定します。
     * Compares trees by their vertex and edge sets.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkTree that)) return false;
        return Objects.equals(vertices, that.vertices) && Objects.equals(edges, that.edges);
    }

    /**
     * 頂点集合と辺集合からハッシュ値を生成します。
     * Computes a hash from the vertex and edge sets.
     */
    @Override
    public int hashCode() {
        return Objects.hash(vertices, edges);
    }

    /**
     * ネットワーク内の接続辺を表します。
     * Represents a connection edge in the network.
     */
    public record Edge(BlockPos parent, BlockPos child, ConnectionMode mode) {

        /**
         * 方向を反転した辺も含めて等価性を判定します。
         * Compares edges regardless of their direction.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Edge(BlockPos f, BlockPos t, ConnectionMode m))) return false;
            return (this.parent.equals(f) && this.child.equals(t)
                    || this.parent.equals(t) && this.child.equals(f))
                    && this.mode == m;
        }

        /**
         * 親子位置を入れ替えた辺を返します。
         * Returns an edge with the parent and child positions swapped.
         */
        public Edge reverse() {
            return new Edge(this.child, this.parent, this.mode);
        }
    }
}
