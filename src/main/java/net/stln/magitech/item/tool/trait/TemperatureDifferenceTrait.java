package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;

import java.util.HashMap;
import java.util.Map;

public class TemperatureDifferenceTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifyStatsConditional1(player, level, stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.25F;
        if (level.isDay()) {
            modified.put(ToolStats.ATK_STAT, stats.getStats().get(ToolStats.ATK_STAT) * mul);
            modified.put(ToolStats.DEF_STAT, stats.getStats().get(ToolStats.DEF_STAT) * mul);
        } else {
            modified.put(ToolStats.SPD_STAT, stats.getStats().get(ToolStats.SPD_STAT) * mul);
        }
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional3(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifySpellCasterStatsConditional3(player, level, stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        float mul = traitLevel * 0.25F;
        if (level.isDay()) {
            modified.put(ToolStats.ATK_STAT, stats.getStats().get(ToolStats.ATK_STAT) * mul);
            modified.put(ToolStats.DEF_STAT, stats.getStats().get(ToolStats.DEF_STAT) * mul);
        } else {
            modified.put(ToolStats.CHG_STAT, stats.getStats().get(ToolStats.CHG_STAT) * mul);
            modified.put(ToolStats.MNA_STAT, stats.getStats().get(ToolStats.MNA_STAT) * mul);
        }
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos) {
        float mul = !level.isDay() ? 1 : traitLevel * 0.25F;
        Float min = stats.getStats().get(ToolStats.MIN_STAT);
        return min * mul;
    }

    @Override
    public int getColor() {
        return 0xFFFFC0;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.temperature_difference");
    }
}
