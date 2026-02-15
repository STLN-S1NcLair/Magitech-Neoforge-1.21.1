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
import net.stln.magitech.api.mana.flow.network.connectable.IManaWirelessWaypoint;
import net.stln.magitech.api.mana.handler.IBlockManaHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class ManaNetworkScanner {

    public static NetworkSnapshot scan(Level level, BlockPos start, @Nullable Direction startSide, boolean isEndpoint, int maxHops) {
        Set<ConnectionKey> visitedWired = new HashSet<>();
        Set<ConnectionKey> visitedWireless = new HashSet<>();

        Set<HandlerEndpoint> endpoints = new HashSet<>();
        Set<BlockPos> waypoints = new HashSet<>();

        Queue<ScanNode> queue = new ArrayDeque<>();


        // 中継点の場合接続可能なすべての方向についてスキャンを開始、Handlerの場合はstartSideの方向でスキャンを開始、無線アクセスの場合はside=nullでスキャンを開始
        if (!isEndpoint) {
            if (level.getBlockState(start).getBlock() instanceof IManaWaypoint connectable) {
                for (ConnectionMode mode : connectable.getConnectableModes(level.getBlockState(start))) {
                    if (mode == ConnectionMode.WIRED) {
                        for (Direction dir : Direction.values()) {
                            if (connectable.getConnectableDirections(level.getBlockState(start)).contains(dir)
                            ) {

                                queue.add(new ScanNode(new ConnectionKey(start, dir), ConnectionMode.WIRED, 0));
                                visitedWired.add(new ConnectionKey(start, dir));
                            }
                        }
                    } else {
                        queue.add(new ScanNode(new ConnectionKey(start, null), mode, 0));
                        visitedWireless.add(new ConnectionKey(start, null));
                    }
                }
            }
        } else {
            if (startSide != null) {
                queue.add(new ScanNode(new ConnectionKey(start, startSide), ConnectionMode.WIRED, 0));
            } else {
                queue.add(new ScanNode(new ConnectionKey(start, null), ConnectionMode.WIRELESS, 0));
                visitedWireless.add(new ConnectionKey(start, null));
            }
        }

        Block startBlock = level.getBlockState(start).getBlock();
        // 中継点
        if (startBlock instanceof IManaWaypoint) {
            waypoints.add(start);
        } else {
            // 開始点が中継点でない場合、Handlerとして収集を試みる
            collectHandler(level, start, endpoints, ConnectionMode.WIRED, startSide);
        }

        while (!queue.isEmpty()) {
            ScanNode node = queue.poll();

            if (node.depth > maxHops) {
                continue;
            }

            BlockPos pos = node.key.pos;
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
    private static void scanWired(Level level, ScanNode node, BlockState state, Queue<ScanNode> queue, Set<ConnectionKey> visitedWired, Set<HandlerEndpoint> endpoints) {
        // 有線モードの探索ロジック
        // 隣接ブロックを調べ、それがConnectorなら有線モードで追加、Nodeならノードとして登録し無線モードと有線モードで追加、Handlerなら終端として登録して終了

        BlockPos pos = node.key.pos;
        Block block = state.getBlock();

        if (!(block instanceof IManaConnectable connectable)) return;

        Set<Direction> nextDirs = node.key.side != null ? Set.of(node.key.side) : Set.of(Direction.values());
        for (Direction dir : nextDirs) {
            BlockPos neighborPos = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);


            // 到達済みチェック
            if (visitedWired.contains(new ConnectionKey(pos, dir))) continue;
            if (visitedWired.contains(new ConnectionKey(neighborPos, dir.getOpposite()))) continue;

            // 接続可能な方向チェック
            Set<Direction> connectableDirs = connectable.getConnectableDirections(state);
            if (!connectableDirs.contains(dir)) {
                continue;
            }

            Block neighborBlock = neighborState.getBlock();
            if (neighborBlock instanceof IManaConnectable connectableNeighbor && connectableNeighbor.getConnectableDirections(neighborState).contains(dir.getOpposite())
                    && connectableNeighbor.getConnectableModes(neighborState).contains(ConnectionMode.WIRED)) {
                if (connectableNeighbor instanceof IManaWaypoint waypoint) {

                    visitedWired.add(new ConnectionKey(pos, dir));
                    visitedWired.add(new ConnectionKey(neighborPos, dir.getOpposite()));

                    // 中継点
                    for (ConnectionMode nextMode : waypoint.getNextScanModes(ConnectionMode.WIRED, dir.getOpposite(), neighborState)) {
                        for (Direction nextDir : waypoint.getConnectableDirections(neighborState)) {
                            if (nextDir == dir.getOpposite() || !visitedWired.add(new ConnectionKey(neighborPos, nextDir))) continue; // 来た方向には戻らない
                            queue.add(new ScanNode(new ConnectionKey(neighborPos, nextDir), nextMode, node.depth + 1));
                        }
                    }
                }
                collectHandler(level, neighborPos, endpoints, ConnectionMode.WIRED, dir.getOpposite());
            }
        }
    }

    private static void scanWireless(Level level, ScanNode node, BlockState state, Queue<ScanNode> queue, Set<ConnectionKey> visitedWireless, Set<HandlerEndpoint> endpoints) {
        // 無線モードの探索ロジック
        // 例: 一定範囲内のブロックを探索し、視認可能かつNodeならノードとして登録し有線モードで追加、無線対応のHandlerなら終端として登録して終了

        Block block = state.getBlock();
        BlockPos pos = node.key.pos;
        if (block instanceof IManaWirelessWaypoint wirelessWaypoint && wirelessWaypoint.getConnectableModes(state).contains(ConnectionMode.WIRELESS)) {
            int range = wirelessWaypoint.getRange();
            int maxConnections = wirelessWaypoint.maxWirelessConnections();
            int connections = 0;
            for (BlockPos targetPos : BlockPos.betweenClosed(pos.offset(-range, -range, -range), pos.offset(range, range, range))) {
                if (connections >= maxConnections) break;
                if (targetPos.equals(pos)) continue;


                // 到達済みチェック
                if (visitedWireless.contains(new ConnectionKey(targetPos, null))) continue;

                // 視認チェック
                if (!canSee(level, pos, targetPos)) continue;

                BlockState targetState = level.getBlockState(targetPos);
                Block targetBlock = targetState.getBlock();
                if (targetBlock instanceof IManaWaypoint waypoint && waypoint.getConnectableModes(targetState).contains(ConnectionMode.WIRELESS)) {

                    visitedWireless.add(new ConnectionKey(targetPos, null));
                    connections++;

                    // 中継点
                    for (ConnectionMode nextMode : waypoint.getNextScanModes(ConnectionMode.WIRELESS, null, targetState)) {
                        queue.add(new ScanNode(new ConnectionKey(targetPos, null), nextMode, node.depth + 1));
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

    // sideがnullの場合、無線アクセスを意味する
    record ConnectionKey(BlockPos pos, @Nullable Direction side) {}

    record ScanNode(ConnectionKey key, ConnectionMode mode, int depth) {}
}
