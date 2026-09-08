package net.stln.magitech.core.api.mana.flow.network;


import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class NetworkTreeHelper {

    public static Set<NetworkTree.Edge> getWirelessPath(NetworkTree networkTree, BlockPos start, BlockPos end) {
        return getPath(networkTree, start, end).stream().filter(edge -> edge.mode() == ConnectionMode.WIRELESS).collect(Collectors.toSet());
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
            NetworkTree.Edge edge = networkTree.getEdge(current);
            if (parent == null || edge == null) {
                return Collections.emptySet();
            }
            pathEdges.add(edge.reverse());
            current = parent;
        }
        // endから共通の親ノードまでのパスを取得
        current = end;
        // root -> leaf
        while (!current.equals(commonParent)) {
            BlockPos parent = networkTree.getParent(current);
            NetworkTree.Edge edge = networkTree.getEdge(current);
            if (parent == null || edge == null) {
                return Collections.emptySet();
            }
            pathEdges.add(edge);
            current = parent;
        }
        return pathEdges;
    }

    public static @Nullable BlockPos findCommonParent(NetworkTree networkTree, BlockPos start, BlockPos end) {
        Set<BlockPos> endParents = new HashSet<>(getParents(networkTree, end));
        endParents.add(end);

        BlockPos current = start;
        while (current != null) {
            if (endParents.contains(current)) {
                return current;
            }
            current = networkTree.getParent(current);
        }
        return null; // 共通の親ノードが見つからない場合はnullを返す(異常)
    }

    // ネットワークツリーから特定のノードの親ノードをすべて取得するヘルパーメソッド
    // 近い親から順にリストで返す
    public static List<BlockPos> getParents(NetworkTree networkTree, BlockPos pos) {
        List<BlockPos> parents = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        BlockPos current = pos;
        while (current != null) {
            BlockPos parent = networkTree.getParent(current);
            if (parent == null || !visited.add(parent)) {
                break; // 親ノードが見つからない、発見済みの場合は終了
            }
            parents.add(parent);
            current = parent;
        }
        return parents;
    }
}
