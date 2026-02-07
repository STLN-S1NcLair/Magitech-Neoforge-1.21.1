package net.stln.magitech.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IBlockManaHandler extends IManaHandler {

    // 受け取り方向、自身の位置、自身のブロック状態
    boolean canReceiveMana(Direction direction, BlockPos pos, BlockState state);

    // 受け取り方向、自身の位置、自身のブロック状態
    boolean canExtractMana(Direction direction, BlockPos pos, BlockState state);

    default void transferManaTick(Level level, BlockPos pos, BlockState state) {
        List<TargetInfo> targetInfos = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockEntity(neighborPos) instanceof IBlockManaHandler target) {
                targetInfos.add(new TargetInfo(target, neighborPos, direction));
            }
        }
        transferMana(level, pos, state, targetInfos);
    }

    default void transferMana(Level level, BlockPos pos, BlockState state, List<TargetInfo> targetInfos) {
        // 隣接ブロックでの受け渡し
        IBlockManaHandler source = this;
        BlockPos sourcePos = pos;

        // --- ステップ1: 参加者の選定と、ネットワーク全体の目標値計算 ---

        long totalMana = source.getEffectiveMana();
        long totalCapacity = source.getMaxMana();

        // 配分候補リスト
        List<TargetInfo> participationList = new ArrayList<>();

        for (TargetInfo targetInfo : targetInfos) {

            if (!(level.getBlockEntity(targetInfo.pos) instanceof IBlockManaHandler target)) continue;

            if (!source.canExtractMana(targetInfo.thisDirection, sourcePos, state) || !target.canReceiveMana(targetInfo.targetDirection, targetInfo.pos, level.getBlockState(targetInfo.pos))) continue;

                // ピンポン防止: 自分より明らかに少ない相手のみ対象
            if (target.getEffectiveFillRatio() < source.getEffectiveFillRatio() - 0.001f) {
                participationList.add(targetInfo);

                totalMana += target.getEffectiveMana();
                totalCapacity += target.getMaxMana();
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

        for (Map.Entry<TargetInfo, Long> entry : demands.entrySet()) {
            TargetInfo info = entry.getKey();
            long rawDemand = entry.getValue(); // ターゲットが欲しがった量

            // 実際に送る量 = 要望量 * 充足率
            long transferAmount = Math.min((long) (rawDemand * supplyRatio), info.handler.getMaxMana() - info.handler.getMana());

            // 閾値判定 (10以下なら送らない)
            if (transferAmount > 10) {
                IManaHandler.transferMana(source, info.handler, transferAmount);
            }
        }
    }

    record TargetInfo(IBlockManaHandler handler, BlockPos pos, Direction thisDirection, Direction targetDirection) {
        public TargetInfo(IBlockManaHandler handler, BlockPos pos, Direction thisDirection) {
            this(handler, pos, thisDirection, thisDirection.getOpposite());
        }
    }
}
