package net.stln.magitech.entity.mob_effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.element.Element;
import net.stln.magitech.entity.status.AttributeInit;

public class MobEffectInit {

    private static final DeferredRegister<MobEffect> STATUS_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Magitech.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> MANA_ADDICTION = STATUS_EFFECTS.register("mana_addiction", id -> new CustomMobEffect(MobEffectCategory.NEUTRAL, 0x40FFF0)
            .addAttributeModifier(AttributeInit.MANA_REGEN, id, 2, AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(AttributeInit.MAX_MANA, id, -50, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> MANA_REGENERATION = STATUS_EFFECTS.register("mana_regeneration", id -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, 0x80FFC0)
            .addAttributeModifier(AttributeInit.MANA_REGEN, id, 2, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> EMBER_POWER = STATUS_EFFECTS.register("ember_power", id -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, Element.EMBER.getColor())
            .addAttributeModifier(AttributeInit.EMBER_SPELL_POWER, id, 0.1, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> GLACE_POWER = STATUS_EFFECTS.register("glace_power", id -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, Element.GLACE.getColor())
            .addAttributeModifier(AttributeInit.GLACE_SPELL_POWER, id, 0.1, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> SURGE_POWER = STATUS_EFFECTS.register("surge_power", id -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, Element.SURGE.getColor())
            .addAttributeModifier(AttributeInit.SURGE_SPELL_POWER, id, 0.1, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> PHANTOM_POWER = STATUS_EFFECTS.register("phantom_power", id -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, Element.PHANTOM.getColor())
            .addAttributeModifier(AttributeInit.PHANTOM_SPELL_POWER, id, 0.1, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> TREMOR_POWER = STATUS_EFFECTS.register("tremor_power", id -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, Element.TREMOR.getColor())
            .addAttributeModifier(AttributeInit.TREMOR_SPELL_POWER, id, 0.1, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> MAGIC_POWER = STATUS_EFFECTS.register("magic_power", id -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, Element.MAGIC.getColor())
            .addAttributeModifier(AttributeInit.MAGIC_SPELL_POWER, id, 0.1, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> FLOW_POWER = STATUS_EFFECTS.register("flow_power", id -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, Element.FLOW.getColor())
            .addAttributeModifier(AttributeInit.FLOW_SPELL_POWER, id, 0.1, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> HOLLOW_POWER = STATUS_EFFECTS.register("hollow_power", id -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, Element.HOLLOW.getColor())
            .addAttributeModifier(AttributeInit.HOLLOW_SPELL_POWER, id, 0.1, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> LEAP_STEP = STATUS_EFFECTS.register("leap_step", id -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, 0xF0F0FF)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, id, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .addAttributeModifier(Attributes.STEP_HEIGHT, id, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    public static final DeferredHolder<MobEffect, MobEffect> SEIZE = STATUS_EFFECTS.register("seize", id -> new CustomMobEffect(MobEffectCategory.HARMFUL, 0xFFFFC0)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, id, -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MobEffect> ECHOLOCATION = STATUS_EFFECTS.register("echolocation", id -> new EcholocationMobEffect(MobEffectCategory.BENEFICIAL, 0x004040));

    public static final DeferredHolder<MobEffect, MobEffect> VOIDROT = STATUS_EFFECTS.register("voidrot", id -> new VoidrotMobEffect(MobEffectCategory.HARMFUL, 0x400080));

    public static final DeferredHolder<MobEffect, MobEffect> PHASELOCK = STATUS_EFFECTS.register("phase_lock", id -> new PhaseLockMobEffect(MobEffectCategory.HARMFUL, 0x200040));

    // 特性用効果

    public static final DeferredHolder<MobEffect, MobEffect> CHARGE = STATUS_EFFECTS.register("charge", id -> new CustomMobEffect(MobEffectCategory.NEUTRAL, Element.SURGE.getColor()));

    public static final DeferredHolder<MobEffect, MobEffect> COOLDOWN = STATUS_EFFECTS.register("cooldown", id -> new CustomMobEffect(MobEffectCategory.NEUTRAL, 0x808080));

    public static void registerMobEffects(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Mob Effect for " + Magitech.MOD_ID);
        STATUS_EFFECTS.register(eventBus);
    }
}
