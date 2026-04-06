package net.stln.magitech.feature.tool.trait;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import net.stln.magitech.content.sound.SoundInit;
import net.stln.magitech.effect.sound.SoundHelper;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.SparkParticleEffect;
import net.stln.magitech.effect.visual.particle.particle_option.ZapParticleEffect;
import net.stln.magitech.effect.visual.preset.LineVFX;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.helper.*;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SparkTrait extends Trait {

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, Entity target) {
        super.onDamageEntity(player, level, stack, traitLevel, properties, target);
        if (player.hasEffect(MobEffectInit.CHARGE)) {
            chain(player, level, traitLevel, target);
        }
        if (!level.isClientSide) {
            TickScheduler.schedule(1, () -> {
            TraitMobEffectHelper.applyTraitMobEffect(player, MobEffectInit.CHARGE, 40, 0);
            }, level.isClientSide);
        }
    }

    private void chain(Player player, Level level, int traitLevel, Entity target) {
        Element element = Element.SURGE;
        float chainRadius = 3.0F + traitLevel * 2.0F;
        float baseDamage = traitLevel * 2.0F + player.getEffect(MobEffectInit.CHARGE).getAmplifier();
        final boolean[] used = {false};
        TickScheduler.schedule(5, () -> {

            if (player.hasEffect(MobEffectInit.CHARGE)) {
                Set<Entity> chainTargets = CombatHelper.getEntitiesInBox(level, target, target.position(), new Vec3(chainRadius, chainRadius, chainRadius)).stream().filter(entity -> entity != player)
                        .filter(entity -> entity.position().distanceTo(target.position()) < chainRadius).collect(Collectors.toSet());

                int i = 0;

                if (!level.isClientSide) {
                    if (!chainTargets.isEmpty()) {
                        float effectiveDamage = baseDamage * DataMapHelper.getElementMultiplier(target, element);
                        for (Entity chainTarget : chainTargets) {
                            if (i++ >= traitLevel * 3) {
                                break;
                            }
                            CombatHelper.applyDamage(player, chainTarget, player.damageSources().source(element.getDamageType(), player), effectiveDamage);
                        }
                        SoundHelper.broadcastSound(level, player, target.position(), Optional.of(SoundInit.ARCLUME));
                        used[0] = true;
                    }
                } else {
                    for (Entity chainTarget : chainTargets) {
                        if (i++ >= traitLevel * 3) {
                            break;
                        }
                        addChainVFX(level, CombatHelper.getBodyPos(target), CombatHelper.getBodyPos(chainTarget), element);
                    }
                }
            }
        }, level.isClientSide);
        if (!level.isClientSide) {
            TickScheduler.schedule(6, () -> {
                if (used[0]) {
                    player.removeEffect(MobEffectInit.CHARGE);
                    player.addEffect(new MobEffectInstance(MobEffectInit.COOLDOWN, 120 / traitLevel, 0));
                }
            }, level.isClientSide);
        }
    }

    protected void addChainVFX(Level level, Vec3 start, Vec3 end, Element element) {
        TrailVFX.zapTrail(level, start, end, 0.5F, 1.0F, 0.5F, 10, element);
        LineVFX.destinationLinedSquare(level, start, end, element, new Section(0F, 1F), 3, 0.0F, 0.1F);
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, properties, blockState, pos, damageAmount, isInitial);
        if (!level.isClientSide) {
            TraitMobEffectHelper.applyTraitMobEffect(player, MobEffectInit.CHARGE, 40, 0);
        }
    }

    @Override
    public Set<BlockPos> additionalBlockBreak(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        int amplifier = 0;
        if (player.hasEffect(MobEffectInit.CHARGE)) {
            amplifier = player.getEffect(MobEffectInit.CHARGE).getAmplifier() + 1;
            if (!level.isClientSide) {
                player.removeEffect(MobEffectInit.CHARGE);
                player.addEffect(new MobEffectInstance(MobEffectInit.COOLDOWN, 240 / traitLevel, 0));
            }
        }
        return BlockHelper.getConnectedBlocks(level, pos, blockState.getBlock(), amplifier * 5);
    }

    @Override
    public boolean effectEnabled(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties) {
        return player.hasEffect(MobEffectInit.CHARGE);
    }

    @Override
    public Color getColor() {
        return new Color(0x80E0FF);
    }

    @Override
    public Color getPrimary() {
        return new Color(0x76FFFE);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x4547B4);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("spark");
    }
}
