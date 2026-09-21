package net.stln.magitech.helper;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.content.entity.mob_effect.MobEffectInit;
import org.jetbrains.annotations.NotNull;

public class TraitMobEffectHelper {

    // クールダウンがあれば得られない
    public static boolean canApplyTraitMobEffect(@NotNull LivingEntity entity) {
        return !entity.hasEffect(MobEffectInit.COOLDOWN);
    }

    public static void applyTraitMobEffect(@NotNull LivingEntity entity, @NotNull Holder<MobEffect> effectHolder, int duration, int amplifier) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        entity.addEffect(new MobEffectInstance(effectHolder, duration, amplifier));
    }

    public static void updateTraitMobEffectDuration(@NotNull LivingEntity entity, @NotNull Holder<MobEffect> effectHolder, int duration) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        updateDuration(entity, effectHolder, duration);
    }

    public static void extendTraitMobEffectDuration(@NotNull LivingEntity entity, @NotNull Holder<MobEffect> effectHolder, int duration) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        extendDuration(entity, effectHolder, duration);
    }

    public static void extendTraitMobEffectAmplifier(@NotNull LivingEntity entity, @NotNull Holder<MobEffect> effectHolder, int amplifier, int duration) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        extendAmplifier(entity, effectHolder, amplifier, duration);
    }

    public static void extendTraitMobEffectAmplifier(@NotNull LivingEntity entity, @NotNull Holder<MobEffect> effectHolder, int amplifier, int maxAmplifier, int duration) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        extendAmplifier(entity, effectHolder, amplifier, maxAmplifier, duration);
    }

    public static void extendTraitMobEffect(@NotNull LivingEntity entity, @NotNull Holder<MobEffect> effectHolder, int amplifier, int maxAmplifier, int duration) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        extend(entity, effectHolder, amplifier, maxAmplifier, duration);
    }

    public static void updateDuration(@NotNull LivingEntity entity, @NotNull Holder<MobEffect> effectHolder, int duration) {
        MobEffectInstance currentEffect = entity.getEffect(effectHolder);
        int amplifier = 0;
        if (currentEffect != null) {
            amplifier = currentEffect.getAmplifier();
            entity.removeEffect(effectHolder);
        }
        entity.addEffect(new MobEffectInstance(effectHolder, duration, amplifier));
    }

    public static void extendDuration(@NotNull LivingEntity entity, @NotNull Holder<MobEffect> effectHolder, int duration) {
        MobEffectInstance currentEffect = entity.getEffect(effectHolder);
        int amplifier = 0;
        int dur = duration;
        if (currentEffect != null) {
            amplifier = currentEffect.getAmplifier();
            dur += currentEffect.getDuration();
            entity.removeEffect(effectHolder);
        }
        entity.addEffect(new MobEffectInstance(effectHolder, dur, amplifier));
    }

    public static void extendAmplifier(@NotNull LivingEntity entity, @NotNull Holder<MobEffect> effectHolder, int amplifier, int duration) {
        extendAmplifier(entity, effectHolder, amplifier, Integer.MAX_VALUE, duration);
    }

    public static void extendAmplifier(@NotNull LivingEntity entity, @NotNull Holder<MobEffect> effectHolder, int amplifier, int maxAmplifier, int duration) {
        MobEffectInstance currentEffect = entity.getEffect(effectHolder);
        if (currentEffect != null) {
            duration = Math.max(duration, currentEffect.getDuration());
            amplifier += currentEffect.getAmplifier();
            entity.removeEffect(effectHolder);
        } else {
            amplifier -= 1;
        }
        amplifier = Math.min(amplifier, maxAmplifier);
        entity.addEffect(new MobEffectInstance(effectHolder, duration, amplifier));
    }

    public static void extend(@NotNull LivingEntity entity, Holder<MobEffect> effectHolder, int amplifier, int maxAmplifier, int duration) {
        extendAmplifier(entity, effectHolder, amplifier, maxAmplifier, duration);
        extendDuration(entity, effectHolder, duration);
    }
}
