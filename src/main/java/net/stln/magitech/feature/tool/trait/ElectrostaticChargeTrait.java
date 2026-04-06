package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.helper.TraitMobEffectHelper;

import java.awt.*;
import java.util.List;

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
        updateCharge(player, level, traitLevel);
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        updateCharge(player, level, traitLevel);
    }

    private static void updateCharge(Player player, Level level, int traitLevel) {
        if (!level.isClientSide) {
            TickScheduler.schedule(1, () -> {
                TraitMobEffectHelper.updateTraitMobEffectDuration(player, MobEffectInit.CHARGE, 40 + traitLevel * 60);
            }, false);
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
    public ResourceLocation getKey() {
        return Magitech.id("electrostatic_charge");
    }
}
