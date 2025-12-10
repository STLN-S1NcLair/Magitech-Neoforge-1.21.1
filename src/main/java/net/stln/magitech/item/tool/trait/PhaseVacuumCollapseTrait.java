package net.stln.magitech.item.tool.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.mob_effect.MobEffectInit;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.VoidGlowParticleEffect;
import net.stln.magitech.util.EffectUtil;
import org.joml.Vector3f;

import java.util.List;

public class PhaseVacuumCollapseTrait extends Trait {

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
        super.onDamageEntity(player, level, stack, traitLevel, stats, target);
        if (target instanceof LivingEntity livingEntity && player.getRandom().nextFloat() > 0.5) {

            livingEntity.addEffect(new MobEffectInstance(MobEffectInit.PHASELOCK, 10 * traitLevel, 0));

            EffectUtil.entityEffect(level, new VoidGlowParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 2F, 1, 0, level.random.nextInt(1, 21), 1.0F), livingEntity, 60);
        }
    }

    @Override
    public void modifyAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, List<ItemAttributeModifiers.Entry> entries) {
        super.modifyAttribute(player, level, stack, traitLevel, stats, entries);
        entries.add(new ItemAttributeModifiers.Entry(Attributes.GRAVITY, new AttributeModifier(Magitech.id("phase_vacuum_collapse"), -0.15 * traitLevel, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.SAFE_FALL_DISTANCE, new AttributeModifier(Magitech.id("phase_vacuum_collapse"), 0.3 * traitLevel, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.FALL_DAMAGE_MULTIPLIER, new AttributeModifier(Magitech.id("phase_vacuum_collapse"), -0.15 * traitLevel, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
    }

    @Override
    public void modifySpellCasterAttribute(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, List<ItemAttributeModifiers.Entry> entries) {
        super.modifySpellCasterAttribute(player, level, stack, traitLevel, stats, entries);
        entries.add(new ItemAttributeModifiers.Entry(Attributes.GRAVITY, new AttributeModifier(Magitech.id("phase_vacuum_collapse"), -0.15 * traitLevel, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.SAFE_FALL_DISTANCE, new AttributeModifier(Magitech.id("phase_vacuum_collapse"), 0.3 * traitLevel, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
        entries.add(new ItemAttributeModifiers.Entry(Attributes.FALL_DAMAGE_MULTIPLIER, new AttributeModifier(Magitech.id("phase_vacuum_collapse"), -0.15 * traitLevel, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.MAINHAND));
    }

    @Override
    public int getColor() {
        return 0x8000FF;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.phase_vacuum_collapse");
    }

}
