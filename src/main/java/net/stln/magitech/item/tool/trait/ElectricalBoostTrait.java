package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import net.stln.magitech.entity.mob_effect.MobEffectInit;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.util.EffectUtil;
import net.stln.magitech.util.TraitMobEffectHelper;
import org.joml.Vector3f;

import java.util.List;

public class ElectricalBoostTrait extends Trait {

    @Override
    public void modifyAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, List<ItemAttributeModifiers.Entry> entries) {
        if (player.hasEffect(MobEffectInit.CHARGE)) {
            entries.add(new ItemAttributeModifiers.Entry(Attributes.MOVEMENT_SPEED, new AttributeModifier(Magitech.id("electrical_boost"), (player.getEffect(MobEffectInit.CHARGE).getAmplifier() + 1) * 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
        }
        super.modifyAttribute(player, level, stack, traitLevel, stats, entries);
    }

    @Override
    public void tick(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, boolean isHost) {
        super.tick(player, level, stack, traitLevel, stats, isHost);
        if (player.hasEffect(MobEffectInit.CHARGE)) {
            EffectUtil.entityEffect(level, new PowerupParticleEffect(new Vector3f(0.8F, 0.9F, 1.0F), new Vector3f(0.65F, 0.7F, 0.85F), 1F, 1, 0, 15, 1.0F), player, 1);
        }
    }

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
        super.onDamageEntity(player, level, stack, traitLevel, stats, target);
        if (!level.isClientSide) {
            TraitMobEffectHelper.extendTraitMobEffectAmplifier(player, MobEffectInit.CHARGE, 1, traitLevel * 2 + 1, 60);
        }
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        if (!level.isClientSide) {
            TraitMobEffectHelper.extendTraitMobEffectAmplifier(player, MobEffectInit.CHARGE, 1, traitLevel * 2 + 1, 60);
        }
    }

    @Override
    public int getColor() {
        return 0xA0A8C0;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.electrical_boost");
    }
}
