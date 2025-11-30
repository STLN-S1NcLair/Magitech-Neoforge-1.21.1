package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class SignalRushTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional3(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (player.getFoodData().getSaturationLevel() > 0) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.3F;
            int signal = Math.max(level.getBestNeighborSignal(player.getOnPos()), level.getSignal(player.getOnPos(), Direction.UP));
            if (signal > 0) {
                mul *= signal * 0.8F + 1;
            }
            Float spd = stats.getStats().get(ToolStats.SPD_STAT);
            modified.put(ToolStats.SPD_STAT, spd * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifyStatsConditional3(player, level, stack, traitLevel, stats);
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional3(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        if (player.getFoodData().getSaturationLevel() > 0) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.3F;
            int signal = Math.max(level.getBestNeighborSignal(player.getOnPos()), level.getSignal(player.getOnPos(), Direction.UP));
            if (signal > 0) {
                mul *= signal * 0.8F + 1;
            }
            Float spd = stats.getStats().get(ToolStats.SPD_STAT);
            modified.put(ToolStats.SPD_STAT, spd * mul);
            Float min = stats.getStats().get(ToolStats.MIN_STAT);
            modified.put(ToolStats.MIN_STAT, min * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel(), aDefault.getTier());
        }
        return super.modifySpellCasterStatsConditional3(player, level, stack, traitLevel, stats);
    }

    @Override
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos) {
        if (player.getFoodData().getSaturationLevel() > 0) {
            float mul = traitLevel * 0.4F;
            int signal = Math.max(level.getBestNeighborSignal(player.getOnPos()), level.getSignal(player.getOnPos(), Direction.UP));
            if (signal > 0) {
                mul *= signal * 0.8F + 1;
            }
            Float min = stats.getStats().get(ToolStats.MIN_STAT);
            return min * mul;
        }
        return super.modifyMiningSpeed(player, level, stack, traitLevel, stats, blockState, pos);
    }

    @Override
    public void onAttackEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
        super.onAttackEntity(player, level, stack, traitLevel, stats, target);
        int signal = Math.max(level.getBestNeighborSignal(player.getOnPos()), level.getSignal(player.getOnPos(), Direction.UP));
        if (signal == 0) {
            player.getFoodData().addExhaustion(1F);
        }
    }

    @Override
    public void onCastSpell(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        super.onCastSpell(player, level, stack, traitLevel, stats);
        int signal = Math.max(level.getBestNeighborSignal(player.getOnPos()), level.getSignal(player.getOnPos(), Direction.UP));
        if (signal == 0) {
            player.getFoodData().addExhaustion(1F);
        }
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        int signal = Math.max(level.getBestNeighborSignal(player.getOnPos()), level.getSignal(player.getOnPos(), Direction.UP));
        if (signal == 0 && isInitial) {
            player.getFoodData().addExhaustion(0.5F);
        }
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        if (player.getFoodData().getSaturationLevel() > 0) {
            int signal = Math.max(level.getBestNeighborSignal(player.getOnPos()), level.getSignal(player.getOnPos(), Direction.UP));
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 0.0F, 0.0F), new Vector3f(1.0F, (float) signal / 20, (float) signal / 20), 1F, 1, 0), player, 1);
        }
    }

    @Override
    public int getColor() {
        return 0xC00000;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.signal_rush");
    }
}
