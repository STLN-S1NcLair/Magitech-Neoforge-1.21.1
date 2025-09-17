package net.stln.magitech.entity.mobeffect;


import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.item.tool.element.Element;

class VoidrotMobEffect extends CustomMobEffect {
    protected VoidrotMobEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getHealth() > livingEntity.getMaxHealth() * 0.25) {
            // Neo: Replace DamageSources#magic() with neoforge:poison to allow differentiating poison damage.
            // Fallback to minecraft:magic in client code when connecting to a vanilla server.
            // LivingEntity#hurt(DamageSource) will no-op in client code immediately, but the holder is resolved before the no-op.
            DamageSource damageSource = livingEntity.damageSources().source(Element.HOLLOW.getDamageType());
            livingEntity.hurt(damageSource, amplifier + 1);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
