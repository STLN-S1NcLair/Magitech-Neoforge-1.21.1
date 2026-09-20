package net.stln.magitech.core.api.mana.flow.network;


import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;

import java.util.*;
import java.util.stream.Collectors;

/**
 * マナネットワーク木の経路や親子関係を計算するヘルパーです。
 * Helper methods for calculating paths and parent relationships in a mana network tree.
 */
public class NetworkTreeHelper {

    /**
     * 2頂点間の経路から無線辺だけを抽出します。
     * Extracts wireless edges from the path between two vertices.
     */
    public static Set<NetworkTree.Edge> getWirelessPath(NetworkTree networkTree, BlockPos start, BlockPos end) {
        return getPath(networkTree, start, end).stream().filter(edge -> edge.mode() == ConnectionMode.WIRELESS).collect(Collectors.toSet());
    }

    /**
     * 2頂点間のネットワーク経路を返します。
     * Returns the network path between two vertices.
     */
    public static Set<NetworkTree.Edge> getPath(NetworkTree networkTree, BlockPos start, BlockPos end) {
        BlockPos commonParent = findCommonParent(networkTree, start, end);
        if (commonParent == null) {
            return Collections.emptySet(); // 共通の親ノードが見つからない場合は空のセットを返す / Return an empty set when no common parent is found
        }
        Set<NetworkTree.Edge> pathEdges = new HashSet<>();
        // startから共通の親ノードまでのパスを取得 / Get the path from start to the common parent
        // start or endのどちらかが共通の親ノードの場合は、片方のパスは空になる / One path is empty when start or end is the common parent
        BlockPos current = start;
        // leaf -> root (逆) / leaf -> root (reverse direction)
        while (!current.equals(commonParent)) {
            BlockPos parent = networkTree.getParent(current);
            pathEdges.add(networkTree.getEdge(current).reverse());
            current = parent;
        }
        // endから共通の親ノードまでのパスを取得 / Get the path from end to the common parent
        current = end;
        // root -> leaf
        while (!current.equals(commonParent)) {
            BlockPos parent = networkTree.getParent(current);
            pathEdges.add(networkTree.getEdge(current));
            current = parent;
        }
        return pathEdges;
    }

    @MethodsReturnNonnullByDefault
    /**
     * 2頂点に共通する最も近い親を返します。
     * Returns the nearest common parent of two vertices.
     */
    public static BlockPos findCommonParent(NetworkTree networkTree, BlockPos start, BlockPos end) {
        List<BlockPos> startParents = getParents(networkTree, start);
        List<BlockPos> endParents = getParents(networkTree, end);
        // 近い親から順に比較して、最初に一致したノードが共通の親ノード / Compare nearest parents first; the first match is the common parent
        for (BlockPos startParent : startParents) {
            for (BlockPos endParent : endParents) {
                if (startParent.equals(endParent)) {
                    return startParent;
                }
            }
        }
        return null; // 共通の親ノードが見つからない場合はnullを返す(異常) / Return null when no common parent is found (unexpected)
    }

    /**
     * 指定頂点の親を近い順にすべて返します。
     * Returns all parents of a vertex, ordered from nearest to farthest.
     */
    public static List<BlockPos> getParents(NetworkTree networkTree, BlockPos pos) {
        List<BlockPos> parents = new ArrayList<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(pos);
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            BlockPos parent = networkTree.getParent(current);
            if (parent == null || parents.contains(parent)) {
                continue; // 親ノードが見つからない、発見済みの場合はスキップ / Skip when no parent exists or it was already found
            }
            parents.add(parent);
            queue.add(parent);
        }
        return parents;
    }
}
