package net.stln.magitech.util;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.mob_effect.MobEffectInit;

public class TraitMobEffectHelper {

    // クールダウンがあれば得られない
    public static boolean canApplyTraitMobEffect(LivingEntity entity) {
        return !entity.hasEffect(MobEffectInit.COOLDOWN);
    }

    public static void applyTraitMobEffect(LivingEntity entity, Holder<MobEffect> effectHolder, int duration, int amplifier) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        entity.addEffect(new MobEffectInstance(effectHolder, duration, amplifier));
    }

    public static void updateTraitMobEffectDuration(LivingEntity entity, Holder<MobEffect> effectHolder, int duration) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        updateDuration(entity, effectHolder, duration);
    }

    public static void extendTraitMobEffectDuration(LivingEntity entity, Holder<MobEffect> effectHolder, int duration) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        extendDuration(entity, effectHolder, duration);
    }

    public static void extendTraitMobEffectAmplifier(LivingEntity entity, Holder<MobEffect> effectHolder, int amplifier, int duration) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        extendAmplifier(entity, effectHolder, amplifier, duration);
    }

    public static void extendTraitMobEffectAmplifier(LivingEntity entity, Holder<MobEffect> effectHolder, int amplifier, int maxAmplifier, int duration) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        extendAmplifier(entity, effectHolder, amplifier, maxAmplifier, duration);
    }

    public static void extendTraitMobEffect(LivingEntity entity, Holder<MobEffect> effectHolder, int amplifier, int maxAmplifier, int duration) {
        if (!canApplyTraitMobEffect(entity)) {
            return;
        }
        extend(entity, effectHolder, amplifier, maxAmplifier, duration);
    }

    public static void updateDuration(LivingEntity entity, Holder<MobEffect> effectHolder, int duration) {
        MobEffectInstance currentEffect = entity.getEffect(effectHolder);
        int amplifier = 0;
        if (currentEffect != null) {
            amplifier = currentEffect.getAmplifier();
            entity.removeEffect(effectHolder);
        }
        entity.addEffect(new MobEffectInstance(effectHolder, duration, amplifier));
    }

    public static void extendDuration(LivingEntity entity, Holder<MobEffect> effectHolder, int duration) {
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

    public static void extendAmplifier(LivingEntity entity, Holder<MobEffect> effectHolder, int amplifier, int duration) {
        extendAmplifier(entity, effectHolder, amplifier, Integer.MAX_VALUE, duration);
    }

    public static void extendAmplifier(LivingEntity entity, Holder<MobEffect> effectHolder, int amplifier, int maxAmplifier, int duration) {
        MobEffectInstance currentEffect = entity.getEffect(effectHolder);
        if (currentEffect != null) {
            duration = Math.max(duration, currentEffect.getDuration());
            amplifier += currentEffect.getAmplifier();
            entity.removeEffect(effectHolder);
        } else {
            amplifier -= 1;
        }
        amplifier = Math.min(amplifier, maxAmplifier);
        Magitech.LOGGER.debug("Extending effect {} to amplifier {} for duration {}", effectHolder.value().getDescriptionId(), amplifier, duration);
        entity.addEffect(new MobEffectInstance(effectHolder, duration, amplifier));
    }

    public static void extend(LivingEntity entity, Holder<MobEffect> effectHolder, int amplifier, int maxAmplifier, int duration) {
        extendAmplifier(entity, effectHolder, amplifier, maxAmplifier, duration);
        extendDuration(entity, effectHolder, duration);
    }
}
