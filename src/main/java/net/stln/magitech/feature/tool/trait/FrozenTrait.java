package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.effect.visual.preset.EntityVFX;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.effect.visual.spawner.ElementParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.property.ToolProperties;

import java.awt.*;

public class FrozenTrait extends Trait {

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, Entity target) {
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.setTicksFrozen(livingEntity.getTicksFrozen() + 80 * traitLevel);
            if (level.isClientSide) {
                PointVFX.burst(level, livingEntity.position().add(0, livingEntity.getBbHeight() / 2, 0), Element.GLACE, ElementParticles::snowParticle, 10, 0.15F);
            }
        }
    }

    @Override
    public Color getColor() {
        return new Color(0xC0FFF9);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("frozen");
    }
}
