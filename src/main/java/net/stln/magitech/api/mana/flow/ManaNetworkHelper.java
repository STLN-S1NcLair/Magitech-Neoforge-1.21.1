package net.stln.magitech.api.mana.flow;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.stln.magitech.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.api.mana.handler.IBlockManaHandler;
import net.stln.magitech.api.mana.handler.IManaConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ManaNetworkHelper {

    // ネットワーク: 始点が送信元、終点が受け入れ先 として探索されるべき

    // コネクター(Mana Junctionなど)を介して接続されたマナハンドラ間のネットワークを探索
    // ノードを使わない部分のマナ輸送などで使用する
    // isStartManaHandler: 開始地点がマナハンドラであるかどうか
    // isStartManaHandlerがfalseの場合、start位置にマナハンドラがなくても探索を開始する(ノードからの探索用)
    // Direction: 探索を開始する方向
    public static Set<IBasicManaHandler> findNetworkWithConnector(Level level, BlockPos start, @NotNull Direction direction, boolean isStartManaHandler) {

        // BFS探索で接続されたマナハンドラをすべて見つける
        Set<BlockPos> visited = new HashSet<>();
        Set<IBasicManaHandler> network = new HashSet<>();
        Queue<BlockPos> toVisit = new LinkedList<>();

        IBlockManaHandler startHandler = ManaTransferHelper.getManaContainer(level, start, direction);
        if (startHandler != null || !isStartManaHandler) {
            BlockPos nextPos = start.relative(direction);
            IBlockManaHandler currentHandler = ManaTransferHelper.getManaContainer(level, nextPos, direction.getOpposite());
            if (currentHandler != null) {
                network.add(currentHandler);
            } else {
                toVisit.add(start.relative(direction));
                visited.add(start);
                visited.add(start.relative(direction));
            }
        }
        while (!toVisit.isEmpty()) {
            BlockPos currentPos = toVisit.poll();
            for (Direction dir : Direction.values()) {
                BlockPos nextPos = currentPos.relative(dir);
                visited.add(nextPos);
                if (visited.contains(nextPos)) continue;
                IBlockManaHandler currentHandler = ManaTransferHelper.getManaContainer(level, nextPos, dir.getOpposite());
                if (currentHandler != null) {
                    network.add(currentHandler);
                } else {
                    if (level.getBlockState(nextPos).getBlock() instanceof IManaConnector) {
                        // 接続ブロックなら探索を続ける
                        toVisit.add(nextPos);
                    }
                }
            }
        }
        return network;
    }

    // ノード同士をつなぐネットワークを探索
    // ノード間のマナ輸送などで使用する
    // ハンドラーは取らず、ノードの経路のみ
    // ノードとハンドラー間の探索ではfindNetworkWithConnectorを使うこと
    // Direction: 探索を開始する方向
    public static Set<NodePath> findNodeNetwork(Level level, BlockPos startNode, @NotNull Direction direction, int maxHops) {

        // 探索用キュー: (現在の座標, ここまでの経路リスト)
        Queue<NodePath> queue = new LinkedList<>();
        // 訪問済みセット
        Set<BlockPos> visited = new HashSet<>();
        // 起点を登録
        List<BlockPos> startPath = new ArrayList<>();
        // 結果リスト
        Set<NodePath> network = new HashSet<>();

        startPath.add(startNode);
        visited.add(startNode);
        queue.add(new NodePath(startNode, startPath));

        while (!queue.isEmpty()) {
            NodePath current = queue.poll();

            // 最大ホップ制限
            if (current.path.size() > maxHops) continue;

            // 周囲を探索
            for (int i = -3; i <= 3; i++) {
                for (int j = -3; j <= 3; j++) {
                    for (int k = -3; k <= 3; k++) {
                        if (i == 0 && j == 0 && k == 0) continue;

                        BlockPos targetPos = current.target.offset(i, j, k);

                        // 訪問済みチェック
                        if (!visited.add(targetPos)) continue;

                        BlockState targetState = level.getBlockState(targetPos);

                        // ノード(IManaNode)であることを確認
                        if (!(targetState.getBlock() instanceof IManaNode)) continue;

                        // 視線チェック (親ノード -> 子ノード)
                        if (!canSee(level, current.target, targetPos)) continue;

                        // 新しい経路リストを作成 (不変性を保つためコピー)
                        List<BlockPos> newPath = new ArrayList<>(current.path);
                        newPath.add(targetPos);

                        // 1. ノードなら結果リストに登録
                        network.add(new NodePath(targetPos, newPath));

                        // 2. リレーなら、さらに先を探索するためにキューに入れる
                        if (targetState.getBlock() instanceof IManaRelay) {
                            queue.add(new NodePath(targetPos, newPath));
                        }
                    }
                }
            }
        }
        return network;
    }

    // ネットワーク更新用ハンドラー位置スナップショットを取得
    public static NetworkSnapshot getConnectorNetworkSnapshot(Level level, BlockPos start, @NotNull Direction direction, int maxHops) {
        Set<BlockPos> manaHandlers = new HashSet<>();
        Set<BlockPos> manaConnectors = new HashSet<>();

        // BFS
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toVisit = new LinkedList<>();
        BlockPos nextPos = start.relative(direction);
        IBlockManaHandler currentHandler = ManaTransferHelper.getManaContainer(level, nextPos, direction.getOpposite());
        if (currentHandler != null) {
            manaHandlers.add(nextPos);
        } else {
            toVisit.add(start.relative(direction));
            visited.add(start);
            visited.add(start.relative(direction));
        }
        while (!toVisit.isEmpty()) {
            BlockPos currentPos = toVisit.poll();
            for (Direction dir : Direction.values()) {
                BlockPos nextPos = currentPos.relative(dir);
                visited.add(nextPos);
                if (visited.contains(nextPos)) continue;
                IBlockManaHandler currentHandler = ManaTransferHelper.getManaContainer(level, nextPos, dir.getOpposite());
                if (canBeSink(currentHandler)) {
                    network.add(currentHandler);
                } else {
                    if (level.getBlockState(nextPos).getBlock() instanceof IManaConnector) {
                        // 接続ブロックなら探索を続ける
                        toVisit.add(nextPos);
                    }
                }
            }
        }

        return new NetworkSnapshot(manaHandlers, manaConnectors, manaRelays);
    }

    private static boolean canSee(Level level, BlockPos startPos, BlockPos endPos) {
        Vec3 start = startPos.getCenter();
        Vec3 end = endPos.getCenter();
        Vec3 dir = end.subtract(start).normalize().scale(0.5);

        BlockHitResult hitResult = level.clip(new ClipContext(
                start.add(dir), end.subtract(dir),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                CollisionContext.empty()
        ));
        return hitResult.getType() == HitResult.Type.MISS;
    }

    public record NodePath(BlockPos target, List<BlockPos> path) {}

    public record NetworkSnapshot(Set<BlockPos> manaHandlers, Set<BlockPos> wayPoints) {
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof NetworkSnapshot(Set<BlockPos> handlers, Set<BlockPos> points))) {
                return false;
            } else {
                return this.manaHandlers.equals(handlers) &&
                       this.wayPoints.equals(points);
            }
        }
    }
}
