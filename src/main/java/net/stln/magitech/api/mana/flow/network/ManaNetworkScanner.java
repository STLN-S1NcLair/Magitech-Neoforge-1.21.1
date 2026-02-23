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
import net.stln.magitech.Magitech;
import net.stln.magitech.api.mana.flow.network.connectable.IManaConnectable;
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

    // startSideがnull: 無線アクセス or 中継点からのスキャン開始、そうでない場合は有線アクセスでのスキャン開始
    public static NetworkSnapshot scan(Level level, BlockPos start, @Nullable Direction startSide, int maxHops) {
        Set<ConnectionKey> visitedWired = new HashSet<>();
        Set<ConnectionKey> visitedWireless = new HashSet<>();

        Set<HandlerEndpoint> endpoints = new HashSet<>();
        Set<BlockPos> waypoints = new HashSet<>();
        Set<NetworkTree.Edge> edges = new HashSet<>();

        Queue<ScanNode> queue = new ArrayDeque<>();


        // 中継点の場合接続可能なすべての方向についてスキャンを開始、Handlerの場合はstartSideの方向でスキャンを開始、無線アクセスの場合はside=nullでスキャンを開始
        if (level.getBlockState(start).getBlock() instanceof IManaWaypoint waypoint) {
            for (ConnectionMode mode : waypoint.getConnectableModes(level.getBlockState(start))) {
                if (mode == ConnectionMode.WIRED) {
                    for (Direction dir : Direction.values()) {
                        if (waypoint.getConnectableDirections(level.getBlockState(start)).contains(dir)
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
        } else {
            if (startSide != null) {
                queue.add(new ScanNode(new ConnectionKey(start, startSide), ConnectionMode.WIRED, 0));
            } else {
                queue.add(new ScanNode(new ConnectionKey(start, null), ConnectionMode.WIRELESS, 0));
                visitedWireless.add(new ConnectionKey(start, null));
            }
        }

        Block startBlock = level.getBlockState(start).getBlock();

        // edge追加: 自己ループであるため、wiredのみでok
        edges.add(new NetworkTree.Edge(start, start, ConnectionMode.WIRED));

        // 中継点
        if (startBlock instanceof IManaWaypoint) {
            waypoints.add(start);
        } else {
            // 開始点が中継点でない場合、Handlerとして収集を試みる
            // rootノードはedgesに追加しない（あくまでHandlerとしての接続を収集するため）
            collectHandler(level, null, start, endpoints, edges, ConnectionMode.WIRED, startSide);
        }

        while (!queue.isEmpty()) {
            ScanNode node = queue.poll();

            // 中継点
            if (!node.key.pos.equals(start)) {
                waypoints.add(node.key.pos);
            }

            if (node.depth > maxHops) {
                continue;
            }

            BlockPos pos = node.key.pos;
            BlockState state = level.getBlockState(pos);

            // モード別探索
            if (node.mode == ConnectionMode.WIRED) {
                scanWired(level, node, state, queue, visitedWired, visitedWireless, endpoints, edges);
            } else {
                scanWireless(level, node, state, queue, visitedWired, visitedWireless, endpoints, edges);
            }
        }
        if (endpoints.isEmpty()) {
            return null; // Handlerが1つも見つからなかった場合はネットワークとして成立しないためnullを返す
        }

        return new NetworkSnapshot(endpoints, waypoints, new NetworkTree(endpoints, waypoints, edges));
    }

    // Handlerを収集できたらHandlerを返す
    private static void scanWired(Level level, ScanNode node, BlockState state, Queue<ScanNode> queue,
                                  Set<ConnectionKey> visitedWired, Set<ConnectionKey> visitedWireless, Set<HandlerEndpoint> endpoints, Set<NetworkTree.Edge> edges) {
        // 有線モードの探索ロジック
        // 隣接ブロックを調べ、それがConnectorなら有線モードで追加、Nodeならノードとして登録し無線モードと有線モードで追加、Handlerなら終端として登録して終了

        BlockPos pos = node.key.pos;
        Block block = state.getBlock();

        if (!(block instanceof IManaConnectable || level.getBlockEntity(pos) instanceof IManaConnectable)) return;

        Set<Direction> nextDirs = node.key.side != null ? Set.of(node.key.side) : Set.of(Direction.values());
        for (Direction dir : nextDirs) {
            BlockPos neighborPos = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);


            // 到達済みチェック
            if (visitedWired.contains(new ConnectionKey(pos, dir))) continue;
            if (visitedWired.contains(new ConnectionKey(neighborPos, dir.getOpposite()))) continue;

            // 接続可能な方向チェック
            IManaConnectable connectable = getConnectable(level, block, pos);
            Set<Direction> connectableDirs = connectable.getConnectableDirections(state);
            if (!connectableDirs.contains(dir)) {
                continue;
            }

            Block neighborBlock = neighborState.getBlock();
            if (neighborBlock instanceof IManaConnectable || level.getBlockEntity(neighborPos) instanceof IManaConnectable) {
                IManaConnectable connectableNeighbor = getConnectable(level, neighborBlock, neighborPos);
                if (connectableNeighbor.getConnectableDirections(neighborState).contains(dir.getOpposite())
                        && connectableNeighbor.getConnectableModes(neighborState).contains(ConnectionMode.WIRED)) {
                    if (connectableNeighbor instanceof IManaWaypoint waypoint) {

                        visitedWired.add(new ConnectionKey(pos, dir));
                        visitedWired.add(new ConnectionKey(neighborPos, dir.getOpposite()));


                        // 中継点
                        edges.add(new NetworkTree.Edge(pos, neighborPos, ConnectionMode.WIRED));
                        addNextQueue(node.depth, queue, visitedWired, visitedWireless, dir, waypoint, neighborState, neighborPos);
                    }
                    collectHandler(level, pos, neighborPos, endpoints, edges, ConnectionMode.WIRED, dir.getOpposite());
                }
            }
        }
    }

    private static void scanWireless(Level level, ScanNode node, BlockState state, Queue<ScanNode> queue,
                                     Set<ConnectionKey> visitedWired, Set<ConnectionKey> visitedWireless, Set<HandlerEndpoint> endpoints, Set<NetworkTree.Edge> edges) {
        // 無線モードの探索ロジック
        // 例: 一定範囲内のブロックを探索し、視認可能かつNodeならノードとして登録し有線モードで追加、無線対応のHandlerなら終端として登録して終了

        Block block = state.getBlock();
        BlockPos pos = node.key.pos;
        if (block instanceof IManaWirelessWaypoint || level.getBlockEntity(pos) instanceof IManaWirelessWaypoint) {

            IManaWirelessWaypoint wirelessWaypoint = (IManaWirelessWaypoint) (block instanceof IManaWirelessWaypoint ? block : level.getBlockEntity(pos));

            if (wirelessWaypoint.getConnectableModes(state).contains(ConnectionMode.WIRELESS)) {
                int range = wirelessWaypoint.getRange();
                for (BlockPos targetPos : BlockPos.betweenClosed(pos.offset(-range, -range, -range), pos.offset(range, range, range))) {
                    if (targetPos.equals(pos)) continue;


                    // 到達済みチェック
                    if (visitedWireless.contains(new ConnectionKey(targetPos, null))) continue;

                    // 視認チェック
                    if (!canSee(level, pos, targetPos)) continue;

                    BlockState targetState = level.getBlockState(targetPos);
                    Block targetBlock = targetState.getBlock();
                    BlockPos nextPos = new BlockPos(targetPos);
                    if (targetBlock instanceof IManaWaypoint waypoint && waypoint.getConnectableModes(targetState).contains(ConnectionMode.WIRELESS)) {

                        // 中継点
                        edges.add(new NetworkTree.Edge(pos, nextPos, ConnectionMode.WIRELESS));
                        addNextQueue(node.depth, queue, visitedWired, visitedWireless, null, waypoint, targetState, nextPos);
                    }
                    collectHandler(level, pos, nextPos, endpoints, edges, ConnectionMode.WIRELESS, null);
                }
            }
        }
    }

    private static void addNextQueue(int depth, Queue<ScanNode> queue, Set<ConnectionKey> visitedWired, Set<ConnectionKey> visitedWireless, Direction dir, IManaWaypoint waypoint, BlockState targetState, BlockPos targetPos) {
        for (ConnectionMode nextMode : waypoint.getConnectableModes(targetState)) {
            if (nextMode == ConnectionMode.WIRED) {
                for (Direction nextDir : waypoint.getConnectableDirections(targetState)) {
                    if (dir != null && nextDir == dir.getOpposite() || visitedWired.contains(new ConnectionKey(targetPos, nextDir)))
                        continue; // 来た方向には戻らない
                    queue.add(new ScanNode(new ConnectionKey(targetPos, nextDir), nextMode, depth + 1));
                }
            } else {
                // 無線モードも接続可能なNodeは無線モードでも追加
                if (visitedWireless.add(new ConnectionKey(targetPos, null))) {
                    queue.add(new ScanNode(new ConnectionKey(targetPos, null), nextMode, depth + 1));
                }
            }
        }
    }

    private static @Nullable IManaConnectable getConnectable(Level level, Block neighborBlock, BlockPos neighborPos) {
        return (IManaConnectable) (neighborBlock instanceof IManaConnectable ? neighborBlock : level.getBlockEntity(neighborPos));
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

    private static void collectHandler(Level level, @Nullable BlockPos from, BlockPos pos, Set<HandlerEndpoint> endpoints, Set<NetworkTree.Edge> edges, ConnectionMode mode, @Nullable Direction side) {
        if (!endpoints.contains(new HandlerEndpoint(pos, side)) && level.getBlockEntity(pos) instanceof IManaConnectable connectable
                && connectable.getConnectableModes(level.getBlockState(pos)).contains(mode)) {
            IBlockManaHandler handler = ManaTransferHelper.getManaContainer(level, pos, side);
            if (handler != null) {
                // 終端
                endpoints.add(new HandlerEndpoint(pos, side));
                if (from != null && !from.equals(pos)) {
                    edges.add(new NetworkTree.Edge(from, pos, mode));
                }
                return;
            }
        }
        return;
    }

    // sideがnullの場合、無線アクセスを意味する
    record ConnectionKey(BlockPos pos, @Nullable Direction side) {}

    record ScanNode(ConnectionKey key, ConnectionMode mode, int depth) {}
}
