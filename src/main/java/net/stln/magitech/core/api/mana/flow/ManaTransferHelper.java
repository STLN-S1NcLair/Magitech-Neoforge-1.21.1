package net.stln.magitech.core.api.mana.flow;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.core.api.mana.handler.IBlockManaHandler;
import net.stln.magitech.core.api.mana.handler.IManaHandler;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ManaTransferHelper {

    public static long transferMana(@Nullable IManaHandler source, @Nullable IManaHandler sink, long amount) {

        // 両方存在すれば転送処理
        if (source != null && sink != null) {

            // シミュレーション: どれだけ抜けるか？
            long extracted = source.extractMana(amount, true);
            // シミュレーション: どれだけ入るか？
            long accepted = sink.insertMana(extracted, true);

            // 実行
            if (accepted > 0) {
                source.extractMana(accepted, false); // 実際に減らす (内部で書き換え)
                sink.insertMana(accepted, false); // 実際に増やす (内部で書き換え)
            }
            return accepted;
        }
        return 0;
    }

    // できる限り多く転送
    public static long transferMana(@Nullable IManaHandler source, @Nullable IManaHandler sink) {
        return transferMana(source, sink, Long.MAX_VALUE);
    }

    // 送信できたハンドラーのセットを返す (転送量はハンドラーごとに異なる)
    public static Set<IBasicManaHandler> balance(IBasicManaHandler source, Set<IBasicManaHandler> sinks) {

        Set<IBasicManaHandler> validSinks = new HashSet<>();

        // --- ステップ1: 参加者の選定と、ネットワーク全体の目標値計算 ---
        BigDecimal totalMana = BigDecimal.valueOf(source.getEffectiveMana());
        BigDecimal totalCapacity = BigDecimal.valueOf(source.getMaxMana());

        // 配分候補リスト
        Set<IBasicManaHandler> sendSet = new HashSet<>();

        for (IBasicManaHandler sink : sinks) {
            // ピンポン防止: 自分より明らかに少なく、満タンでない相手のみ対象
            if (sink.getEffectiveFillRatio() < source.getEffectiveFillRatio() - 0.001f && sink.fillRatio() < 1.0F) {
                sendSet.add(sink);

                totalMana = totalMana.add(BigDecimal.valueOf(sink.getEffectiveMana()));
                totalCapacity = totalCapacity.add(BigDecimal.valueOf(sink.getMaxMana()));
            }
        }

        if (sendSet.isEmpty()) return Set.of();

        if (totalCapacity.signum() <= 0) return Set.of();

        // ネットワーク全体の目標充填率
        BigDecimal targetRatioDecimal = totalMana.divide(totalCapacity, MathContext.DECIMAL128);
        BigDecimal clampedTargetRatio = targetRatioDecimal.max(BigDecimal.ZERO).min(BigDecimal.ONE);

        // --- ステップ2: 各ターゲットの「要望量(Demand)」を計算 ---

        // Key: ターゲット, Value: そのターゲットが欲しがっている量(MaxFlow考慮済み)
        Map<IBasicManaHandler, BigDecimal> demands = new HashMap<>();
        BigDecimal totalDemand = BigDecimal.ZERO;

        for (IBasicManaHandler target : sendSet) {

            // 目標量まであといくら必要か
            BigDecimal targetIdeal = BigDecimal.valueOf(target.getMaxMana()).multiply(clampedTargetRatio, MathContext.DECIMAL128);
            BigDecimal required = targetIdeal.subtract(BigDecimal.valueOf(target.getEffectiveMana()));

            if (required.signum() > 0) {
                // ターゲット側の受入流量制限 (Pipeの太さ)
                BigDecimal demand = required.min(BigDecimal.valueOf(target.getMaxFlow()));

                demands.put(target, demand);
                totalDemand = totalDemand.add(demand);
            }
        }

        if (totalDemand.signum() <= 0) return Set.of();

        // --- ステップ3: ソースの「供給能力(Supply)」と「分配比率(Ratio)」の計算 ---

        // ソースが維持すべき理想量
        BigDecimal sourceIdeal = BigDecimal.valueOf(source.getMaxMana()).multiply(clampedTargetRatio, MathContext.DECIMAL128);
        // 放出可能な余剰分
        BigDecimal excessMana = BigDecimal.valueOf(source.getEffectiveMana()).subtract(sourceIdeal);

        // 実際に放出できる量 = Min(余剰分, ソースの最大流量)
        BigDecimal distributableMana = excessMana.min(BigDecimal.valueOf(source.getMaxFlow()));

        if (distributableMana.signum() <= 0) return Set.of();

        // 充足率 (1.0 = 全員の要望を満たせる, 0.5 = 半分しかあげられない)
        BigDecimal supplyRatio = distributableMana.divide(totalDemand, MathContext.DECIMAL128)
                .max(BigDecimal.ZERO)
                .min(BigDecimal.ONE);

        for (Map.Entry<IBasicManaHandler, BigDecimal> entry : demands.entrySet()) {
            IBasicManaHandler target = entry.getKey();
            BigDecimal rawDemand = entry.getValue(); // ターゲットが欲しがった量

            // 実際に送る量 = 要望量 * 充足率
            BigDecimal transfer = rawDemand.multiply(supplyRatio, MathContext.DECIMAL128)
                    .min(BigDecimal.valueOf(target.getMaxMana() - target.getMana()))
                    .min(BigDecimal.valueOf(source.getMana()));
            long transferAmount = transfer.longValue();

            // 閾値判定 (10以下なら送らない)
            if (transferAmount > 10) {
                ManaTransferHelper.transferMana(source, target, transferAmount);
                validSinks.add(target);
            }
        }
        return validSinks;
    }

    public static @Nullable IBlockManaHandler getManaContainer(Level level, BlockPos pos, @Nullable Direction direction) {
        return level.getCapability(ManaCapabilities.MANA_CONTAINER, pos, level.getBlockState(pos), level.getBlockEntity(pos), direction);
    }
}