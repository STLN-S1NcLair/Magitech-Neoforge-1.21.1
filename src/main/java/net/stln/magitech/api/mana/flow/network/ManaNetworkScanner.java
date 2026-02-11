package net.stln.magitech.api.mana.flow.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.stln.magitech.api.mana.flow.network.connectable.IManaConnectable;
import net.stln.magitech.api.mana.flow.network.connectable.IManaRelay;
import net.stln.magitech.api.mana.flow.network.connectable.IManaWaypoint;
import net.stln.magitech.api.mana.flow.ManaTransferHelper;
import net.stln.magitech.api.mana.handler.IBlockManaHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class ManaNetworkScanner {

    public static NetworkSnapshot scan(Level level, BlockPos start, int maxHops) {
        Set<BlockPos> visitedWired = new HashSet<>();
        Set<BlockPos> visitedWireless = new HashSet<>();

        Set<HandlerEndpoint> endpoints = new HashSet<>();
        Set<BlockPos> waypoints = new HashSet<>();

        Queue<ScanNode> queue = new ArrayDeque<>();

        queue.add(new ScanNode(start, ConnectionMode.WIRED, 0));

        Block startBlock = level.getBlockState(start).getBlock();
        // 中継点
        if (startBlock instanceof IManaWaypoint) {
            waypoints.add(start);
        } else {
            // 開始点が中継点でない場合、Handlerとして収集を試みる
            collectHandler(level, start, endpoints, ConnectionMode.WIRED, null);
        }

        while (!queue.isEmpty()) {
            ScanNode node = queue.poll();

            if (node.depth > maxHops) {
                continue;
            }

            BlockPos pos = node.pos;
            BlockState state = level.getBlockState(pos);

            // モード別探索
            if (node.mode == ConnectionMode.WIRED) {
                scanWired(level, node, state, queue, visitedWired, endpoints);
            } else {
                scanWireless(level, node, state, queue, visitedWireless, endpoints);
            }
        }
        return new NetworkSnapshot(endpoints, waypoints);
    }

    // Handlerを収集できたらHandlerを返す
    private static void scanWired(Level level, ScanNode node, BlockState state, Queue<ScanNode> queue, Set<BlockPos> visitedWired, Set<HandlerEndpoint> endpoints) {
        // 有線モードの探索ロジック
        // 隣接ブロックを調べ、それがConnectorなら有線モードで追加、Nodeならノードとして登録し無線モードと有線モードで追加、Handlerなら終端として登録して終了

        BlockPos pos = node.pos;
        Block block = state.getBlock();

        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);


            // 到達済みチェック
            if (visitedWired.contains(neighborPos)) continue;

            if (block instanceof IManaConnectable connectable) {
                // 接続可能な方向チェック
                Set<Direction> connectableDirs = connectable.getConnectableDirections(state);
                if (!connectableDirs.contains(dir)) {
                    continue;
                }
            }

            Block neighborBlock = neighborState.getBlock();
            if (neighborBlock instanceof IManaWaypoint waypoint && waypoint.getConnectableDirections(neighborState).contains(dir.getOpposite())
                    && waypoint.getConnectableModes(neighborState).contains(ConnectionMode.WIRED)) {
                // 中継点
                for (ConnectionMode nextMode : waypoint.getNextScanModes(ConnectionMode.WIRED, dir.getOpposite(), neighborState)) {
                    queue.add(new ScanNode(neighborPos, nextMode, node.depth + 1));
                    visitedWired.add(neighborPos);
                }
            }
            collectHandler(level, neighborPos, endpoints, ConnectionMode.WIRED, dir.getOpposite());
        }
    }

    private static void scanWireless(Level level, ScanNode node, BlockState state, Queue<ScanNode> queue, Set<BlockPos> visitedWireless, Set<HandlerEndpoint> endpoints) {
        // TODO: 無線モードの探索ロジック
        // 例: 一定範囲内のブロックを探索し、視認可能かつNodeならノードとして登録し有線モードで追加、無線対応のHandlerなら終端として登録して終了

        Block block = state.getBlock();
        if (block instanceof IManaRelay relay && relay.getConnectableModes(state).contains(ConnectionMode.WIRELESS)) {
            int range = relay.getRange();
            for (BlockPos targetPos : BlockPos.betweenClosed(node.pos.offset(-range, -range, -range), node.pos.offset(range, range, range))) {
                if (targetPos.equals(node.pos)) continue;


                // 到達済みチェック
                if (visitedWireless.contains(targetPos)) continue;

                // 視認チェック
                if (!canSee(level, node.pos, targetPos)) continue;

                BlockState targetState = level.getBlockState(targetPos);
                Block targetBlock = targetState.getBlock();
                if (targetBlock instanceof IManaWaypoint waypoint && waypoint.getConnectableModes(targetState).contains(ConnectionMode.WIRELESS)) {
                    // 中継点
                    for (ConnectionMode nextMode : waypoint.getNextScanModes(ConnectionMode.WIRELESS, null, targetState)) {
                        queue.add(new ScanNode(targetPos, nextMode, node.depth + 1));
                        visitedWireless.add(targetPos);
                    }
                }
                collectHandler(level, targetPos, endpoints, ConnectionMode.WIRELESS, null);
            }
        }
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

    // Handlerを収集できたらHandlerを返す
    private static void collectHandler(Level level, BlockPos pos, Set<HandlerEndpoint> endpoints, ConnectionMode mode, @Nullable Direction side) {
        if (!endpoints.contains(new HandlerEndpoint(pos, side)) && level.getBlockEntity(pos) instanceof IManaConnectable connectable
                && connectable.getConnectableModes(level.getBlockState(pos)).contains(mode)) {
            IBlockManaHandler handler = ManaTransferHelper.getManaContainer(level, pos, side);
            if (handler != null) {
                // 終端
                endpoints.add(new HandlerEndpoint(pos, side));
                return;
            }
        }
    }

    record ScanNode(BlockPos pos, ConnectionMode mode, int depth) {}
}
