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

/**
 * マナハンドラー間の転送とバランス調整を行うヘルパーです。
 * Helper methods for transferring and balancing mana between mana handlers.
 */
public class ManaTransferHelper {

    /**
     * 指定量のマナをソースからシンクへ転送します。
     * Transfers the specified amount of mana from a source to a sink.
     *
     * @return 実際に転送された量 / the amount actually transferred
     */
    public static long transferMana(@Nullable IManaHandler source, @Nullable IManaHandler sink, long amount) {

        // 両方存在すれば転送処理 / Transfer mana when both handlers exist
        if (source != null && sink != null) {

            // シミュレーション: どれだけ抜けるか？ / Simulation: how much can be extracted?
            long extracted = source.extractMana(amount, true);
            // シミュレーション: どれだけ入るか？ / Simulation: how much can be inserted?
            long accepted = sink.insertMana(extracted, true);

            // 実行 / Execute the transfer
            if (accepted > 0) {
                source.extractMana(accepted, false); // 実際に減らす (内部で書き換え) / Actually subtracts the amount (mutates internally)
                sink.insertMana(accepted, false); // 実際に増やす (内部で書き換え) / Actually adds the amount (mutates internally)
            }
            return accepted;
        }
        return 0;
    }

    /**
     * 可能な限り多くのマナを転送します。
     * Transfers as much mana as possible.
     *
     * @return 実際に転送された量 / the amount actually transferred
     */
    public static long transferMana(@Nullable IManaHandler source, @Nullable IManaHandler sink) {
        return transferMana(source, sink, Long.MAX_VALUE);
    }

    /**
     * ソースの余剰マナをシンク間でバランスさせます。
     * Balances excess mana from a source across the sinks.
     *
     * @return マナを受け取ったハンドラーの集合 / handlers that received mana
     */
    public static Set<IBasicManaHandler> balance(IBasicManaHandler source, Set<IBasicManaHandler> sinks) {

        Set<IBasicManaHandler> validSinks = new HashSet<>();

        // --- ステップ1: 参加者の選定と、ネットワーク全体の目標値計算 / Step 1: select participants and calculate the network-wide target ---
        BigDecimal totalMana = BigDecimal.valueOf(source.getEffectiveMana());
        BigDecimal totalCapacity = BigDecimal.valueOf(source.getMaxMana());

        // 配分候補リスト / Candidate list for distribution
        Set<IBasicManaHandler> sendSet = new HashSet<>();

        for (IBasicManaHandler sink : sinks) {
            // ピンポン防止: 自分より明らかに少なく、満タンでない相手のみ対象 / Prevent ping-pong: target only handlers that are clearly lower and not full
            if (sink.getEffectiveFillRatio() < source.getEffectiveFillRatio() - 0.001f && sink.fillRatio() < 1.0F) {
                sendSet.add(sink);

                totalMana = totalMana.add(BigDecimal.valueOf(sink.getEffectiveMana()));
                totalCapacity = totalCapacity.add(BigDecimal.valueOf(sink.getMaxMana()));
            }
        }

        if (sendSet.isEmpty()) return Set.of();

        if (totalCapacity.signum() <= 0) return Set.of();

        // ネットワーク全体の目標充填率 / Network-wide target fill ratio
        BigDecimal targetRatioDecimal = totalMana.divide(totalCapacity, MathContext.DECIMAL128);
        BigDecimal clampedTargetRatio = targetRatioDecimal.max(BigDecimal.ZERO).min(BigDecimal.ONE);

        // --- ステップ2: 各ターゲットの「要望量(Demand)」を計算 / Step 2: calculate each target's demand ---

        // Key: ターゲット, Value: そのターゲットが欲しがっている量(MaxFlow考慮済み) / Key: target, Value: requested amount after considering MaxFlow
        Map<IBasicManaHandler, BigDecimal> demands = new HashMap<>();
        BigDecimal totalDemand = BigDecimal.ZERO;

        for (IBasicManaHandler target : sendSet) {

            // 目標量まであといくら必要か / Amount still required to reach the target
            BigDecimal targetIdeal = BigDecimal.valueOf(target.getMaxMana()).multiply(clampedTargetRatio, MathContext.DECIMAL128);
            BigDecimal required = targetIdeal.subtract(BigDecimal.valueOf(target.getEffectiveMana()));

            if (required.signum() > 0) {
                // ターゲット側の受入流量制限 (Pipeの太さ) / Target-side insertion flow limit (pipe capacity)
                BigDecimal demand = required.min(BigDecimal.valueOf(target.getMaxFlow()));

                demands.put(target, demand);
                totalDemand = totalDemand.add(demand);
            }
        }

        if (totalDemand.signum() <= 0) return Set.of();

        // --- ステップ3: ソースの「供給能力(Supply)」と「分配比率(Ratio)」の計算 / Step 3: calculate source supply and the distribution ratio ---

        // ソースが維持すべき理想量 / Ideal amount that the source should retain
        BigDecimal sourceIdeal = BigDecimal.valueOf(source.getMaxMana()).multiply(clampedTargetRatio, MathContext.DECIMAL128);
        // 放出可能な余剰分 / Distributable excess amount
        BigDecimal excessMana = BigDecimal.valueOf(source.getEffectiveMana()).subtract(sourceIdeal);

        // 実際に放出できる量 = Min(余剰分, ソースの最大流量) / Actual distributable amount = min(excess amount, source maximum flow)
        BigDecimal distributableMana = excessMana.min(BigDecimal.valueOf(source.getMaxFlow()));

        if (distributableMana.signum() <= 0) return Set.of();

        // 充足率 (1.0 = 全員の要望を満たせる, 0.5 = 半分しかあげられない) / Supply ratio (1.0 = all demands can be met, 0.5 = only half can be supplied)
        BigDecimal supplyRatio = distributableMana.divide(totalDemand, MathContext.DECIMAL128)
                .max(BigDecimal.ZERO)
                .min(BigDecimal.ONE);

        for (Map.Entry<IBasicManaHandler, BigDecimal> entry : demands.entrySet()) {
            IBasicManaHandler target = entry.getKey();
            BigDecimal rawDemand = entry.getValue(); // ターゲットが欲しがった量 / Amount requested by the target

            // 実際に送る量 = 要望量 * 充足率 / Actual transfer amount = demand * supply ratio
            BigDecimal transfer = rawDemand.multiply(supplyRatio, MathContext.DECIMAL128)
                    .min(BigDecimal.valueOf(target.getMaxMana() - target.getMana()))
                    .min(BigDecimal.valueOf(source.getMana()));
            long transferAmount = transfer.longValue();

            // 閾値判定 (10以下なら送らない) / Threshold check (do not transfer 10 or less)
            if (transferAmount > 10) {
                ManaTransferHelper.transferMana(source, target, transferAmount);
                validSinks.add(target);
            }
        }
        return validSinks;
    }

    /**
     * 指定ブロックのマナ容器ハンドラーを取得します。
     * Gets the mana-container handler at a block position.
     */
    public static @Nullable IBlockManaHandler getManaContainer(Level level, BlockPos pos, @Nullable Direction direction) {
        return level.getCapability(ManaCapabilities.MANA_CONTAINER, pos, level.getBlockState(pos), level.getBlockEntity(pos), direction);
    }
}
