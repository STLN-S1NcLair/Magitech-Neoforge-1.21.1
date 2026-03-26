package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.effect.visual.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.SingleToolPropertyGroup;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.ToolPropertyInit;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.TraitMobEffectHelper;
import org.joml.Vector3f;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElectrostaticChargeTrait extends Trait {

    @Override
    public List<ToolPropertyModifier> modifyProperty(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        List<ToolPropertyModifier> list = super.modifyProperty(player, level, stack, traitLevel, properties);
        float value = 0.25F * traitLevel;
        list.add(new RationalToolPropertyModifier(ToolPropertyCategory.ELEMENT, value));
        list.add(new RationalToolPropertyModifier(ToolPropertyCategory.CONTINUITY, value));
        if (!effectEnabled(player, level, stack, traitLevel, properties)) {
            for (ToolPropertyModifier modifier : list) {
                modifier.setEnabled(false);
            }
        }
        return list;
    }

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, Entity target) {
        super.onDamageEntity(player, level, stack, traitLevel, properties, target);
        if (!level.isClientSide) {
            TraitMobEffectHelper.updateTraitMobEffectDuration(player, MobEffectInit.CHARGE, 40 + traitLevel * 60);
        }
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        if (!level.isClientSide) {
            TraitMobEffectHelper.updateTraitMobEffectDuration(player, MobEffectInit.CHARGE, 40 + traitLevel * 60);
        }
    }

    @Override
    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        return player.hasEffect(MobEffectInit.CHARGE);
    }

    @Override
    public Color getColor() {
        return new Color(0xB8D0A0);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xB8D0A0);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x688577);
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.electrostatic_charge");
    }
}
