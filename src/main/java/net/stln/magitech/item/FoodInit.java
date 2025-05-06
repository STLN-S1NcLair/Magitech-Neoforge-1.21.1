package net.stln.magitech.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.stln.magitech.entity.mobeffect.MobEffectInit;

public class FoodInit {

    public static final FoodProperties MANA_BERRY = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.1F)
            .effect(() -> new MobEffectInstance(MobEffectInit.MANA_ADDICTION, 100, 0), 1.0F)
            .alwaysEdible()
            .build();
}
