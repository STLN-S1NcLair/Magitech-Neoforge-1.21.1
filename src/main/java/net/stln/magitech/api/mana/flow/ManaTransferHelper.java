package net.stln.magitech.api.mana.flow;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.api.ManaCapabilities;
import net.stln.magitech.api.mana.flow.network.NetworkTree;
import net.stln.magitech.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.api.mana.handler.IBlockManaHandler;
import net.stln.magitech.api.mana.handler.IManaHandler;
import net.stln.magitech.network.ManaNodeTransferPayload;
import net.stln.magitech.sound.SoundInit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
        long totalMana = source.getEffectiveMana();
        long totalCapacity = source.getMaxMana();

        // 配分候補リスト
        Set<IBasicManaHandler> sendSet = new HashSet<>();

        for (IBasicManaHandler sink : sinks) {
            // ピンポン防止: 自分より明らかに少ない相手のみ対象
            if (sink.getEffectiveFillRatio() < source.getEffectiveFillRatio() - 0.001f) {
                sendSet.add(sink);

                totalMana += sink.getEffectiveMana();
                totalCapacity += sink.getMaxMana();
            }
        }

        if (sendSet.isEmpty()) return Set.of();

        // ネットワーク全体の目標充填率
        double targetRatio = (double) totalMana / totalCapacity;

        // --- ステップ2: 各ターゲットの「要望量(Demand)」を計算 ---

        // Key: ターゲット, Value: そのターゲットが欲しがっている量(MaxFlow考慮済み)
        Map<IBasicManaHandler, Long> demands = new HashMap<>();
        long totalDemand = 0;

        for (IBasicManaHandler target : sendSet) {

            // 目標量まであといくら必要か
            long targetIdeal = (long) (target.getMaxMana() * targetRatio);
            long required = targetIdeal - target.getEffectiveMana();

            if (required > 0) {
                // ターゲット側の受入流量制限 (Pipeの太さ)
                long demand = Math.min(required, target.getMaxFlow());

                demands.put(target, demand);
                totalDemand += demand;
            }
        }

        if (totalDemand <= 0) return Set.of();

        // --- ステップ3: ソースの「供給能力(Supply)」と「分配比率(Ratio)」の計算 ---

        // ソースが維持すべき理想量
        long sourceIdeal = (long) (source.getMaxMana() * targetRatio);
        // 放出可能な余剰分
        long excessMana = source.getEffectiveMana() - sourceIdeal;

        // 実際に放出できる量 = Min(余剰分, ソースの最大流量)
        long distributableMana = Math.min(excessMana, source.getMaxFlow());

        if (distributableMana <= 0) return Set.of();

        // 充足率 (1.0 = 全員の要望を満たせる, 0.5 = 半分しかあげられない)
        double supplyRatio = (double) distributableMana / totalDemand;

        // 1.0を超えないようにキャップ (需要より供給が多い場合は1.0)
        supplyRatio = Math.min(supplyRatio, 1.0d);

        for (Map.Entry<IBasicManaHandler, Long> entry : demands.entrySet()) {
            IBasicManaHandler target = entry.getKey();
            long rawDemand = entry.getValue(); // ターゲットが欲しがった量

            // 実際に送る量 = 要望量 * 充足率
            long transferAmount = Math.min((long) (rawDemand * supplyRatio), target.getMaxMana() - target.getMana());

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