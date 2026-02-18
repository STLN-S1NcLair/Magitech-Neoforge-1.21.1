package net.stln.magitech.api.mana.flow.network;


import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;

import java.util.*;
import java.util.stream.Collectors;

public class NetworkTreeHelper {

    public static Set<NetworkTree.Edge> getWirelessPath(NetworkTree networkTree, BlockPos start, BlockPos end) {
        return  getPath(networkTree, start, end).stream().filter(edge -> edge.mode() == ConnectionMode.WIRELESS).collect(Collectors.toSet());
    }

    public static Set<NetworkTree.Edge> getPath(NetworkTree networkTree, BlockPos start, BlockPos end) {
        BlockPos commonParent = findCommonParent(networkTree, start, end);
        if (commonParent == null) {
            return Collections.emptySet(); // 共通の親ノードが見つからない場合は空のセットを返す
        }
        Set<NetworkTree.Edge> pathEdges = new HashSet<>();
        // startから共通の親ノードまでのパスを取得
        // start or endのどちらかが共通の親ノードの場合は、片方のパスは空になる
        BlockPos current = start;
        // leaf -> root (逆)
        while (!current.equals(commonParent)) {
            BlockPos parent = networkTree.getParent(current);
            pathEdges.add(networkTree.getEdge(current).reverse());
            current = parent;
        }
        // endから共通の親ノードまでのパスを取得
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
    public static BlockPos findCommonParent(NetworkTree networkTree, BlockPos start, BlockPos end) {
        List<BlockPos> startParents = getParents(networkTree, start);
        List<BlockPos> endParents = getParents(networkTree, end);
        // 近い親から順に比較して、最初に一致したノードが共通の親ノード
        for (BlockPos startParent : startParents) {
            for (BlockPos endParent : endParents) {
                if (startParent.equals(endParent)) {
                    return startParent;
                }
            }
        }
        return null; // 共通の親ノードが見つからない場合はnullを返す(異常)
    }

    // ネットワークツリーから特定のノードの親ノードをすべて取得するヘルパーメソッド
    // 近い親から順にリストで返す
    public static List<BlockPos> getParents(NetworkTree networkTree, BlockPos pos) {
        List<BlockPos> parents = new ArrayList<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(pos);
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            BlockPos parent = networkTree.getParent(current);
            parents.add(parent);
            queue.add(parent);
        }
        return parents;
    }
}
