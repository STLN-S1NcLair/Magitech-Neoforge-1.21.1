package net.stln.magitech.item.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.element.Element;
import net.stln.magitech.entity.mob_effect.MobEffectInit;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;
import net.stln.magitech.particle.particle_option.SparkParticleEffect;
import net.stln.magitech.particle.particle_option.ZapParticleEffect;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.util.*;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SparkTrait extends Trait {

    @Override
    public void onDamageEntity(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, Entity target) {
        super.onDamageEntity(player, level, stack, traitLevel, stats, target);
        if (!level.isClientSide) {
            TraitMobEffectHelper.applyTraitMobEffect(player, MobEffectInit.CHARGE, 40, 0);
        }
    }

    @Override
    public void onBreakBlock(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.onBreakBlock(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        if (!level.isClientSide) {
            TraitMobEffectHelper.applyTraitMobEffect(player, MobEffectInit.CHARGE, 40, 0);
        }
    }

    @Override
    public void traitAction(Player player, Level level, Entity target, Vec3 lookingPos, ItemStack stack, int traitLevel, ToolStats stats, InteractionHand hand, boolean isHost) {
        super.traitAction(player, level, target, lookingPos, stack, traitLevel, stats, hand, isHost);

        if (player.hasEffect(MobEffectInit.CHARGE)) {
            for (int i = 0; i < (player.getEffect(MobEffectInit.CHARGE).getAmplifier() + 1) * 3; i++) {
                TickScheduler.schedule(i, () -> {

                    Vec3 forward = Vec3.directionFromRotation(player.getRotationVector());
                    double maxReachLength = 12.0;
                    double radius = 0.1;
                    Vec3 hitPos = EntityUtil.raycastBeam(player, maxReachLength, radius);

                    ResourceKey<DamageType> damageType = Element.SURGE.getDamageType();
                    DamageSource elementalDamageSource = player.damageSources().source(damageType, player);
                    Vec3 start = player.position().add(0, player.getBbHeight() * 0.7, 0).add(forward.scale(0.5));

                    Entity target1 = EntityUtil.raycastBeamEntity(player, maxReachLength, radius);
                    if (target1 != null) {
                        float finalDamage = (float) (1.0 * DataMapHelper.getElementMultiplier(target1, Element.SURGE));
                        if (target1.isAttackable()) {
                            if (target1 instanceof LivingEntity livingTarget) {
                                if (!target1.isInvulnerableTo(elementalDamageSource)) {
                                    float targetHealth = livingTarget.getHealth();
                                    livingTarget.setLastHurtByMob(player);
                                    player.awardStat(Stats.DAMAGE_DEALT, Math.round((targetHealth - livingTarget.getHealth()) * 10));
                                }
                                livingTarget.invulnerableTime = 0;
                                target1.hurt(elementalDamageSource, finalDamage);
                            }
                            player.setLastHurtMob(target1);
                        }
                    }
                    addVisualEffect(level, player, start, hitPos);
                    playBeamSound(level, player);

                }, level.isClientSide);
            }
            if (!level.isClientSide && !isHost) {
                player.removeEffect(MobEffectInit.CHARGE);
                player.addEffect(new MobEffectInstance(MobEffectInit.COOLDOWN, 240 / traitLevel, 0));
            }
        }
    }

    protected void playBeamSound(Level level, Player user) {
        level.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.ARCLUME.get(), SoundSource.PLAYERS, 1.0F, 0.4F + (user.getRandom().nextFloat() * 0.4F));
    }

    protected void addVisualEffect(Level level, Player user, Vec3 start, Vec3 hitPos) {
        EffectUtil.lineEffect(level, new SparkParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), 0.5F, 3, 0), start, hitPos, 2, false);
            level.addParticle(new ZapParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F),
                            new Vector3f((float) (hitPos.x), (float) (hitPos.y), (float) (hitPos.z)), 1.0F, 1, 0),
                    start.x, start.y, start.z, 0, 0, 0);
        level.addParticle(new BeamParticleEffect(new Vector3f(0.15F, 0.15F, 0.2F), new Vector3f(0.1F, 0.1F, 0.2F), hitPos.toVector3f(), 0.5F, 1, 1), start.x, start.y, start.z, 0, 0, 0);
    }

    @Override
    public Set<BlockPos> addAdditionalBlockBreakFirst(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, Direction direction) {
        if (player.hasEffect(MobEffectInit.CHARGE)) {
            if (!level.isClientSide) {
                player.removeEffect(MobEffectInit.CHARGE);
                player.addEffect(new MobEffectInstance(MobEffectInit.COOLDOWN, 240 / traitLevel, 0));
            }
            return BlockUtil.getConnectedBlocks(level, pos, blockState.getBlock(), (player.getEffect(MobEffectInit.CHARGE).getAmplifier() + 1) * 5);
        }
        return new HashSet<>();
    }

    @Override
    public boolean emitEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        return !isInitial;
    }

    @Override
    public void addEffect(Player player, Level level, ItemStack stack, int traitLevel, ToolStats stats, BlockState blockState, BlockPos pos, int damageAmount, boolean isInitial) {
        super.addEffect(player, level, stack, traitLevel, stats, blockState, pos, damageAmount, isInitial);
        Vec3 center = pos.getCenter();
        level.addParticle(new ZapParticleEffect(new Vector3f(1.0F, 1.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F),
                        new Vector3f((float) (center.x), (float) (center.y), (float) (center.z)), 1.0F, 1, 0),
                player.position().x, player.position().y, player.position().z, 0, 0, 0);
        level.playSound(player, pos, SoundInit.SPARK.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (player.getRandom().nextFloat() * 0.4F));
    }

    @Override
    public int getColor() {
        return 0x80E0FF;
    }

    @Override
    public Component getName() {
        return Component.translatable("trait.magitech.spark");
    }
}
