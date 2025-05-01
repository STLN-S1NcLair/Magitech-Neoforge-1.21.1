package net.stln.magitech.entity.statuseffect;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.status.AttributeInit;

public class StatusEffectInit {

    public static final DeferredRegister<MobEffect> STATUS_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Magitech.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> MANA_ADDICTION = STATUS_EFFECTS.register("mana_addiction", () -> new MobEffect(MobEffectCategory.NEUTRAL, 0x40FFF0)
            .addAttributeModifier(AttributeInit.MANA_REGEN.get(), ))
}
