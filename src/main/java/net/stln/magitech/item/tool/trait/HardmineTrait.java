package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.HashMap;
import java.util.Map;

public class HardmineTrait extends Trait {

    @Override
    public ToolStats modifySpellCasterStats1(ItemStack stack, int traitLevel, ToolStats stats) {
        ToolStats aDefault = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(aDefault.getStats());
        float mul = traitLevel * 0.1F;
        Float cld = PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.CLD_STAT);
        modified.put(ToolStats.CLD_STAT, -cld * mul);
        Float chg = PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.CHG_STAT);
        modified.put(ToolStats.CHG_STAT, -chg * mul);
        Float pwr = PartToolItem.getDefaultStats(stack).getStats().get(ToolStats.PWR_STAT);
        modified.put(ToolStats.PWR_STAT, pwr * mul);
        return new ToolStats(modified, stats.getElement(), stats.getMiningLevel());
    }

    @Override
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos) {
        return blockState.getBlock().getExplosionResistance() * traitLevel / 5;
    }

    @Override
    public void onAttackEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
        if (target instanceof LivingEntity livingEntity && player.getRandom().nextFloat() < traitLevel * 0.2F) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
        }
    }

    @Override
    public int getColor() {
        return 0x404050;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.hardmine");
    }
}
