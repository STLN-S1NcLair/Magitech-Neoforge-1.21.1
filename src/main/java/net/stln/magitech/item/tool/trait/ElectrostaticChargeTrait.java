package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.entity.mob_effect.MobEffectInit;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.TraitMobEffectHelper;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ElectrostaticChargeTrait extends Trait {

    @Override
    public ToolStats modifyStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifyStatsConditional1(player, level, stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        if (player.hasEffect(MobEffectInit.CHARGE)) {
            float mul = traitLevel * 0.25F;
            modified.put(ToolStats.ELM_ATK_STAT, stats.getStats().get(ToolStats.ELM_ATK_STAT) * mul);
            modified.put(ToolStats.MIN_STAT, stats.getStats().get(ToolStats.MIN_STAT) * mul);
        }
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public ToolStats modifySpellCasterStatsConditional1(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats) {
        super.modifySpellCasterStatsConditional1(player, level, stack, traitLevel, stats);
        ToolStats defaultStats = ToolStats.DEFAULT;
        Map<String, Float> modified = new HashMap<>(defaultStats.getStats());
        if (player.hasEffect(MobEffectInit.CHARGE)) {
            float mul = traitLevel * 0.25F;
            modified.put(ToolStats.ELM_ATK_STAT, stats.getStats().get(ToolStats.ELM_ATK_STAT) * mul);
            modified.put(ToolStats.CLD_STAT, stats.getStats().get(ToolStats.CLD_STAT) * mul);
        }
        return new ToolStats(modified, defaultStats.getElement(), defaultStats.getMiningLevel(), defaultStats.getTier());
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        if (player.hasEffect(MobEffectInit.CHARGE)) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.75F, 0.9F, 0.6F), new Vector3f(0.65F, 0.9F, 0.4F), 1F, 1, 0, 15, 1.0F), player, 1);
        }
    }

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
        super.onDamageEntity(player, level, stack, traitLevel, stats, target);
        if (!level.isClientSide) {
            TraitMobEffectHelper.updateTraitMobEffectDuration(player, MobEffectInit.CHARGE, 40 + traitLevel * 60);
        }
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        if (!level.isClientSide) {
            TraitMobEffectHelper.updateTraitMobEffectDuration(player, MobEffectInit.CHARGE, 40 + traitLevel * 60);
        }
    }

    @Override
    public int getColor() {
        return 0xB8D0A0;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.electrostatic_charge");
    }
}
