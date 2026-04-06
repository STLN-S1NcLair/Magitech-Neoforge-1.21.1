package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.helper.TickScheduler;
import net.stln.magitech.helper.TraitMobEffectHelper;

import java.awt.*;
import java.util.List;

public class ElectricalBoostTrait extends Trait {

    @Override
    public void modifyAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, List<ItemAttributeModifiers.Entry> entries) {
        if (player.hasEffect(MobEffectInit.CHARGE)) {
            entries.add(new ItemAttributeModifiers.Entry(Attributes.MOVEMENT_SPEED, new AttributeModifier(Magitech.id("electrical_boost"),
                    (player.getEffect(MobEffectInit.CHARGE).getAmplifier() + 1) * 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
        }
        super.modifyAttribute(player, level, stack, traitLevel, properties, entries);
    }

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, Entity target) {
        super.onDamageEntity(player, level, stack, traitLevel, properties, target);
        extendCharge(player, level, traitLevel);
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, properties, blockState, pos, damageAmount, isInitial);
        extendCharge(player, level, traitLevel);
    }

    private static void extendCharge(Player player, Level level, int traitLevel) {
        if (!level.isClientSide) {
            TickScheduler.schedule(1, () -> {
            TraitMobEffectHelper.extendTraitMobEffectAmplifier(player, MobEffectInit.CHARGE, 1, traitLevel * 2 + 1, 60);
            }, false);
        }
    }

    @Override
    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        return player.hasEffect(MobEffectInit.CHARGE);
    }

    @Override
    public Color getColor() {
        return new Color(0xA0A8C0);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xC3ECFF);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x9384B3);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("electrical_boost");
    }
}
