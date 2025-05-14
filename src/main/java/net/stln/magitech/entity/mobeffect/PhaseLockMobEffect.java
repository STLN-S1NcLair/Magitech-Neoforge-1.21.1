package net.stln.magitech.entity.mobeffect;


import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.stln.magitech.item.tool.Element;

class PhaseLockMobEffect extends CustomMobEffect {
    protected PhaseLockMobEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.setDeltaMovement(0, 0, 0);
        livingEntity.setPos(livingEntity.xOld, livingEntity.yOld, livingEntity.zOld);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
