package net.stln.magitech.entity.mobeffect;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.status.AttributeInit;

public class MobEffectInit {

    private static final DeferredRegister<MobEffect> STATUS_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Magitech.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> MANA_ADDICTION = STATUS_EFFECTS.register("mana_addiction", () -> new CustomMobEffect(MobEffectCategory.NEUTRAL, 0x40FFF0)
            .addAttributeModifier(AttributeInit.MANA_REGEN, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mana_addiction"), 2, AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(AttributeInit.MAX_MANA, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mana_addiction"), -50, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> LEAP_STEP = STATUS_EFFECTS.register("leap_step", () -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, 0xF0F0FF)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "leap_step"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.STEP_HEIGHT, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "leap_step"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    public static final DeferredHolder<MobEffect, MobEffect> SEIZE = STATUS_EFFECTS.register("seize", () -> new CustomMobEffect(MobEffectCategory.HARMFUL, 0xFFFFC0)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "seize"), -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MobEffect> ECHOLOCATION = STATUS_EFFECTS.register("echolocation", () -> new EcholocationMobEffect(MobEffectCategory.BENEFICIAL, 0x004040));

    public static final DeferredHolder<MobEffect, MobEffect> VOIDROT = STATUS_EFFECTS.register("voidrot", () -> new VoidrotMobEffect(MobEffectCategory.HARMFUL, 0x400080));

    public static final DeferredHolder<MobEffect, MobEffect> PHASELOCK = STATUS_EFFECTS.register("phase_lock", () -> new PhaseLockMobEffect(MobEffectCategory.HARMFUL, 0x200040));

    public static void registerMobEffects(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Mob Effect for " + Magitech.MOD_ID);
        STATUS_EFFECTS.register(eventBus);
    }
}
