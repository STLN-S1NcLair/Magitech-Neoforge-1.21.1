package net.stln.magitech.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.block.ManaNodeBlock;
import net.stln.magitech.block.ManaRelayBlock;
import net.stln.magitech.network.ManaNodeTransferPayload;
import net.stln.magitech.sound.SoundInit;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ManaNodeLogicHelper {
    private final BlockEntity host;
    private final @Nullable IBlockManaHandler selfContainer;

    // ターゲットへの経路マップ (TargetPos -> [Source, Relay1, Relay2, ..., Target])
    private final Map<BlockPos, List<BlockPos>> cachedPaths = new HashMap<>();

    private boolean needsRescan = true;
    private int tickCount = 0;

    // 最大ホップ数 (リレーを経由できる回数)
    private static final int MAX_HOPS = 64;

    public ManaNodeLogicHelper(BlockEntity host, @Nullable IBlockManaHandler selfContainer) {
        this.host = host;
        this.selfContainer = selfContainer;
        this.tickCount += new Random().nextInt(40); // スキャンタイミングをずらす
    }

    public void requestRescan() {
        this.needsRescan = true;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;
        tickCount++;

        // 1. 定期スキャン (1秒に1回)
        if (needsRescan || tickCount % 20 == 0) {
            scanNetwork(level, pos);
            needsRescan = false;
        }

        // 2. ソースの取得
        IBlockManaHandler source = getSourceContainer(level, state, pos);
        if (source == null || source.getMana() <= 0) return;

        // 3. ネットワーク平衡化と転送
        balanceNetwork(level, pos, source);
    }

    /**
     * ネットワーク探索 (BFS)
     * リレーを経由して到達可能なすべてのターゲットへの「経路」を記録する
     */
    private void scanNetwork(Level level, BlockPos startPos) {
        cachedPaths.clear();

        // 探索用キュー: (現在の座標, ここまでの経路リスト)
        Queue<PathNode> queue = new LinkedList<>();
        // 訪問済みセット
        Set<BlockPos> visited = new HashSet<>();

        // 起点を登録
        List<BlockPos> startPath = new ArrayList<>();
        startPath.add(startPos);

        visited.add(startPos);
        queue.add(new PathNode(startPos, startPath));

        while (!queue.isEmpty()) {
            PathNode current = queue.poll();

            // 最大ホップ制限
            if (current.path.size() > MAX_HOPS) continue;

            // 周囲を探索
            for (int i = -3; i <= 3; i++) {
                for (int j = -3; j <= 3; j++) {
                    for (int k = -3; k <= 3; k++) {
                        if (i == 0 && j == 0 && k == 0) continue;

                        BlockPos targetPos = current.pos.offset(i, j, k);


                        BlockState targetState = level.getBlockState(targetPos);
                        BlockEntity targetBe = level.getBlockEntity(targetPos);

                        // ノード(IManaNode)であることを確認
                        if (!(targetState.getBlock() instanceof ManaNodeBlock)) continue;

                        // 視線チェック (親ノード -> 子ノード)
                        if (!canSee(level, current.pos, targetPos)) continue;

                        // 訪問済みチェック
                        if (!visited.add(targetPos)) continue;

                        // 新しい経路リストを作成 (不変性を保つためコピー)
                        List<BlockPos> newPath = new ArrayList<>(current.path);
                        newPath.add(targetPos);

                        // 1. コンテナを持っているか確認 (ターゲット候補として登録)
                        // isReceive = true (搬入可能なもの)
                        IBlockManaHandler container = getHandlerFromNode(level, targetPos, targetState, true);
                        if (container != null) {
                            // 自分自身へのパスは登録しない
                            if (!targetPos.equals(startPos)) {
                                cachedPaths.put(targetPos, newPath);
                            }
                        }

                        // 2. リレーなら、さらに先を探索するためにキューに入れる
                        // (IManaNodeかつIBlockManaHandlerならリレーとみなす、またはクラスチェック)
                        if (targetState.getBlock() instanceof ManaRelayBlock) {
                            queue.add(new PathNode(targetPos, newPath));
                        }
                    }
                }
            }
        }
    }

    /**
     * ネットワーク全体を一括で平衡化する (公平分配・流量制限対応版)
     */
    private void balanceNetwork(Level level, BlockPos sourcePos, IBlockManaHandler source) {
        if (cachedPaths.isEmpty()) return;

        // --- ステップ1: 参加者の選定と、ネットワーク全体の目標値計算 ---

        long totalMana = source.getEffectiveMana();
        long totalCapacity = source.getMaxMana();

        // 配分候補リスト
        List<TargetInfo> participationList = new ArrayList<>();

        for (Map.Entry<BlockPos, List<BlockPos>> entry : cachedPaths.entrySet()) {
            BlockPos targetPos = entry.getKey();
            if (targetPos.equals(sourcePos)) continue;

            IBlockManaHandler target = getHandlerFromNode(level, targetPos, level.getBlockState(targetPos), true);

            if (target != null) {
                // ピンポン防止: 自分より明らかに少ない相手のみ対象
                if (target.getEffectiveFillRatio() < source.getEffectiveFillRatio() - 0.001f) {
                    participationList.add(new TargetInfo(target, targetPos, entry.getValue()));

                    totalMana += target.getEffectiveMana();
                    totalCapacity += target.getMaxMana();
                }
            }
        }

        if (participationList.isEmpty()) return;

        // ネットワーク全体の目標充填率
        double targetRatio = (double) totalMana / totalCapacity;

        // --- ステップ2: 各ターゲットの「要望量(Demand)」を計算 ---

        // Key: ターゲット, Value: そのターゲットが欲しがっている量(MaxFlow考慮済み)
        Map<TargetInfo, Long> demands = new HashMap<>();
        long totalDemand = 0;

        for (TargetInfo info : participationList) {
            IBlockManaHandler target = info.handler;

            // 目標量まであといくら必要か
            long targetIdeal = (long) (target.getMaxMana() * targetRatio);
            long required = targetIdeal - target.getEffectiveMana();

            if (required > 0) {
                // ターゲット側の受入流量制限 (Pipeの太さ)
                long demand = Math.min(required, target.getMaxFlow());

                demands.put(info, demand);
                totalDemand += demand;
            }
        }

        if (totalDemand <= 0) return;

        // --- ステップ3: ソースの「供給能力(Supply)」と「分配比率(Ratio)」の計算 ---

        // ソースが維持すべき理想量
        long sourceIdeal = (long) (source.getMaxMana() * targetRatio);
        // 放出可能な余剰分
        long excessMana = source.getEffectiveMana() - sourceIdeal;

        // 実際に放出できる量 = Min(余剰分, ソースの最大流量)
        long distributableMana = Math.min(excessMana, source.getMaxFlow());

        if (distributableMana <= 0) return;

        // 充足率 (1.0 = 全員の要望を満たせる, 0.5 = 半分しかあげられない)
        double supplyRatio = (double) distributableMana / totalDemand;

        // 1.0を超えないようにキャップ (需要より供給が多い場合は1.0)
        supplyRatio = Math.min(supplyRatio, 1.0d);

        // --- ステップ4: 比率に基づいて分配実行 ---

        boolean playedSound = false;

        for (Map.Entry<TargetInfo, Long> entry : demands.entrySet()) {
            TargetInfo info = entry.getKey();
            long rawDemand = entry.getValue(); // ターゲットが欲しがった量

            // 実際に送る量 = 要望量 * 充足率
            long transferAmount = Math.min((long) (rawDemand * supplyRatio), info.handler.getMaxMana() - info.handler.getMana());

            // 閾値判定 (10以下なら送らない)
            if (transferAmount > 10) {
                long accepted = IManaHandler.transferMana(source, info.handler, transferAmount);

                if (accepted > 0) {
                    // エフェクト送信
                    spawnPathParticles(level, info.path);
                    playedSound = true;
                }
            }
        }
    }

    /**
     * 経路リストに従って、各区間ごとにパケットを送信する
     */
    private void spawnPathParticles(Level level, List<BlockPos> path) {
        // パスは [Start, Relay1, Relay2, ..., End] の順
        for (int i = 0; i < path.size() - 1; i++) {
            BlockPos from = path.get(i);
            BlockPos to = path.get(i + 1);
                if (level.random.nextFloat() < 0.5f) {
            PacketDistributor.sendToPlayersTrackingChunk(
                    (ServerLevel) level,
                    new ChunkPos(from),
                    new ManaNodeTransferPayload(from, to)
            );
        }
        }
        if (tickCount % 30 == 0) {
            Vec3 midPoint = Vec3.atCenterOf(path.getFirst()).add(Vec3.atCenterOf(path.getLast())).scale(0.5);
            level.playSound(null, midPoint.x, midPoint.y, midPoint.z, SoundInit.MANA_NODE.get(), SoundSource.BLOCKS, 0.3F, 1.0F);
        }
    }

    // --- 以下、ヘルパーメソッド ---

    private record PathNode(BlockPos pos, List<BlockPos> path) {}
    private record TargetInfo(IBlockManaHandler handler, BlockPos pos, List<BlockPos> path) {}

    private @Nullable IBlockManaHandler getSourceContainer(Level level, BlockState state, BlockPos pos) {
        if (this.selfContainer != null) {
            return this.selfContainer;
        }
        return getNeighborContainer(level, state, pos, false);
    }

    public static @Nullable IBlockManaHandler getHandlerFromNode(Level level, BlockPos nodePos, BlockState nodeState, boolean isReceive) {
        BlockEntity be = level.getBlockEntity(nodePos);
        if (be instanceof IBlockManaHandler handler) {
            return handler;
        }
        return getNeighborContainer(level, nodeState, nodePos, isReceive);
    }

    public static @Nullable IBlockManaHandler getNeighborContainer(Level level, BlockState nodeState, BlockPos nodePos, boolean isReceive) {
        if (!nodeState.hasProperty(ManaNodeBlock.FACING)) return null;
        Direction facing = nodeState.getValue(ManaNodeBlock.FACING);
        BlockPos containerPos = nodePos.relative(facing.getOpposite());
        BlockEntity containerBe = level.getBlockEntity(containerPos);

        if (containerBe instanceof IBlockManaHandler handler) {
            if (isReceive) {
                if (handler.canReceiveMana(facing, containerPos, level.getBlockState(containerPos))) return handler;
            } else {
                if (handler.canExtractMana(facing, containerPos, level.getBlockState(containerPos))) return handler;
            }
        }
        return null;
    }

    private boolean canSee(Level level, BlockPos startPos, BlockPos endPos) {
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
}