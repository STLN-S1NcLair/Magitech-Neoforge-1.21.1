package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.LevelStem;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ShatterforceTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
            ToolStats aDefault = ToolStats.DEFAULT;
            Map<String, Float> modified = new HashMap<>(aDefault.getStats());
            float mul = traitLevel * 0.5F * stack.getDamageValue() / stack.getMaxDamage();
            Float swp = PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.SWP_STAT);
            modified.put(ToolStats.SWP_STAT, swp * mul);
            return new ToolStats(modified, stats.getElement(), stats.getMiningLevel());
    }

    @Override
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos) {
            float mul = traitLevel * 0.25F * stack.getDamageValue() / stack.getMaxDamage();
            Float min = PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.MIN_STAT);
            return min * mul;
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        super.tick(player, level, stack, traitLevel, stats);
        if (player.getRandom().nextFloat() < (float) stack.getDamageValue() / stack.getMaxDamage()) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(1.0F, 0.7F, 0.9F), new Vector3f(1.0F, 0.7F, 0.9F), 1F, 1), player, 1);
        }
    }

    @Override
    public int getColor() {
        return 0xFFC0F0;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.shatterforce");
    }
}
