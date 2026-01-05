package net.stln.magitech.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.ManaNodeBlock;
import net.stln.magitech.block.block_entity.ManaNodeBlockEntity;
import net.stln.magitech.block.block_entity.ManaRelayBlockEntity;
import net.stln.magitech.network.ManaNodeTransferPayload;
import net.stln.magitech.sound.SoundInit;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ManaNodeLogicHelper {
    private final BlockEntity host; // 自分自身 (Node または Relay)
    private final Set<BlockPos> cachedNodePositions = new HashSet<>();
    private boolean needsRescan = true;
    private int tickCount = 0;

    // ★追加: 有効な送信先が存在するかどうかのフラグ
    private boolean hasValidTarget = false;

    // コンテナ
    private final @Nullable IBlockManaHandler selfContainer;

    // コンストラクタ
    public ManaNodeLogicHelper(BlockEntity host, @Nullable IBlockManaHandler selfContainer) {
        this.host = host;
        this.selfContainer = selfContainer;
    }

    public void requestRescan() {
        this.needsRescan = true;
    }

    // ★外部から状態を確認するためのメソッド
    public boolean hasValidTarget() {
        return hasValidTarget;
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;
        tickCount++;

        if (needsRescan || tickCount % 20 == 0) {
            rescanNeighbors(level, pos);
            needsRescan = false;
        }

        IBlockManaHandler sourceContainer = getSourceContainer(level, state, pos);

        if (sourceContainer == null) return;

        boolean playSound = false;
        // 転送処理
        for (BlockPos targetNodePos : cachedNodePositions) {
            BlockState targetState = level.getBlockState(targetNodePos);
            if (!(level.getBlockEntity(targetNodePos) instanceof IManaNode)) continue;

            // 3. 相手の接続先コンテナを取得 (あちらへ注入する)
            // ※ ここでは「搬入(Receive)」が可能かチェックする
            IBlockManaHandler targetContainer = getHandlerFromNode(level, targetNodePos, targetState, true); // true = Receiveチェック

            playSound = transfer((ServerLevel) level, pos, targetNodePos, targetContainer, sourceContainer, playSound);
        }

        if (playSound && tickCount % 5 == 0) {
            level.playSound(null, pos, SoundInit.ATHANOR_PILLAR_INFUSION.get(), SoundSource.BLOCKS, 0.07F, 1.0F);
        }
    }

    private static boolean transfer(ServerLevel level, BlockPos pos, BlockPos targetNodePos, IBlockManaHandler targetContainer, IBlockManaHandler sourceContainer, boolean succeeded) {
        // 4. 転送実行
        // まずは充填率が高い方から低い方へ
        if (targetContainer != null && sourceContainer.fillRatio() > targetContainer.fillRatio()) {

            // --- ステップ1: 理想的な平衡状態を計算 ---
            long totalCurrent = sourceContainer.getMana() + targetContainer.getMana();
            long totalMax = sourceContainer.getMaxMana() + targetContainer.getMaxMana();

            // 2つのタンクを合わせた「全体の平均充填率」
            // (doubleキャストを忘れずに)
            double targetRatio = (double) totalCurrent / totalMax;

            // この充填率になるために、自分が持っているべき理想のマナ量
            long myIdealMana = (long) (targetRatio * sourceContainer.getMaxMana());

            // 「今の量」から「理想の量」を引いたもの = 余剰分（送るべき量）
            long equalizeAmount = sourceContainer.getMana() - myIdealMana;

            // --- ステップ2: 流量制限 ---
            // 一度に送れるのは「計算上の必要量」か「ノードの最大流量」の小さい方
            long maxFlow = sourceContainer.getMaxFlow();
            long transferAmount = Math.min(equalizeAmount / 2, maxFlow);

            // --- ステップ3: 閾値 (不感帯) ---
            // 移動量が少なすぎる(10以下)なら、もう誤差の範囲として転送しない
            if (transferAmount > 10) {
                IManaHandler.transferMana(sourceContainer, targetContainer, transferAmount);
                PacketDistributor.sendToPlayersTrackingChunk(
                        level,
                        new ChunkPos(pos),
                        new ManaNodeTransferPayload(pos, targetNodePos)
                );
                succeeded = true;
            }
        }
        return succeeded;
    }

    /**
     * 送信元のコンテナを取得する
     */
    private @Nullable IBlockManaHandler getSourceContainer(Level level, BlockState state, BlockPos pos) {
        // ★ もし自分自身がコンテナとして登録されているなら、それを返す (Relay用)
        if (this.selfContainer != null) {
            return this.selfContainer;
        }

        // ★ そうでないなら、隣接ブロックを探す (Node用)
        // false = Extract可能かチェック
        return getNeighborContainer(level, state, pos, false);
    }

    /**
     * 指定したノードに隣接するコンテナを取得する (汎用メソッド)
     * (旧 getConnectedManaContainer を改名)
     */
    public static @Nullable IBlockManaHandler getNeighborContainer(Level level, BlockState nodeState, BlockPos nodePos, boolean isReceive) {
        // ノードの向きを取得
        if (!nodeState.hasProperty(ManaNodeBlock.FACING)) return null;
        Direction facing = nodeState.getValue(ManaNodeBlock.FACING);

        // 反対側のブロックをチェック
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

    private void rescanNeighbors(Level level, BlockPos pos) {
        cachedNodePositions.clear();
        this.hasValidTarget = false; // 一旦リセット

        // 範囲探索 (半径3マス = 7x7x7)
        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                for (int k = -3; k < 4; k++) {
                    if (i == 0 && j == 0 && k == 0) continue;

                    BlockPos offsetPos = pos.offset(i, j, k);
                    BlockState offsetState = level.getBlockState(offsetPos);

                    // ManaNodeBlockであるか
                    BlockEntity blockEntity = level.getBlockEntity(offsetPos);
                    if (!(blockEntity instanceof IManaNode)) {
                        continue;
                    }

                    // 視線が通るか
                    if (!canSee(level, pos, offsetPos)) {
                        continue;
                    }

                    // 【追加】相手のノードが、搬入可能なコンテナを持っているか確認
                    // 持っていないノードをリストに入れても意味がないため
                    IBlockManaHandler targetContainer = getHandlerFromNode(level, offsetPos, offsetState, true);

                    if (targetContainer != null) {
                        cachedNodePositions.add(offsetPos);
                    }
                }
            }
        }

        // リストが空でなければ、送信先ありとみなす
        this.hasValidTarget = !cachedNodePositions.isEmpty();
    }

    /**
     * 指定座標にあるノード(またはリレー)がアクセス可能なコンテナを取得する
     * * @param level レベル
     * @param nodePos ノードがある座標
     * @param nodeState ノードのBlockState
     * @param isReceive trueなら搬入可能か、falseなら搬出可能かチェック
     */
    public static @Nullable IBlockManaHandler getHandlerFromNode(Level level, BlockPos nodePos, BlockState nodeState, boolean isReceive) {
        BlockEntity be = level.getBlockEntity(nodePos);

        // パターンA: 相手がコンテナを持つ場合 (BE自体がハンドラを実装している)
        if (be instanceof IBlockManaHandler handler) {
            return handler;
        }

        // パターンB: 相手が「通常ノード」の場合 (BEはハンドラではない)
        // 隣接するブロック(タンク等)を探しに行く
        return getNeighborContainer(level, nodeState, nodePos, isReceive);
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
