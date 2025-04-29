package net.stln.magitech.entity.status;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AttributeInit {

    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, Magitech.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> MAX_MANA = ATTRIBUTES.register("max_mana", () -> (new RangedAttribute("attribute.magitech.max_mana", 100, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> MANA_REGEN = ATTRIBUTES.register("mana_regen", () -> (new RangedAttribute("attribute.magitech.mana_regen", 2, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> MAX_NOCTIS = ATTRIBUTES.register("max_noctis", () -> (new RangedAttribute("attribute.magitech.max_noctis", 50, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> NOCTIS_REGEN = ATTRIBUTES.register("noctis_regen", () -> (new RangedAttribute("attribute.magitech.noctis_regen", 0.5, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> MAX_LUMINIS = ATTRIBUTES.register("max_luminis", () -> (new RangedAttribute("attribute.magitech.max_luminis", 50, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> LUMINIS_REGEN = ATTRIBUTES.register("luminis_regen", () -> (new RangedAttribute("attribute.magitech.luminis_regen", 0.5, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> MAX_FLUXIA = ATTRIBUTES.register("max_fluxia", () -> (new RangedAttribute("attribute.magitech.max_fluxia", 50, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> FLUXIA_REGEN = ATTRIBUTES.register("fluxia_regen", () -> (new RangedAttribute("attribute.magitech.fluxia_regen", 0.5, 0, Double.MAX_VALUE).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> SPELL_POWER = ATTRIBUTES.register("spell_power", () -> (new PercentageAttribute("attribute.magitech.spell_power", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> CASTING_SPEED = ATTRIBUTES.register("casting_speed", () -> (new PercentageAttribute("attribute.magitech.casting_speed", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> COOLDOWN_SPEED = ATTRIBUTES.register("cooldown_speed", () -> (new PercentageAttribute("attribute.magitech.cooldown_speed", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> PROJECTILE_SPEED = ATTRIBUTES.register("projectile_speed", () -> (new PercentageAttribute("attribute.magitech.projectile_speed", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> MANA_EFFICIENCY = ATTRIBUTES.register("mana_efficiency", () -> (new PercentageAttribute("attribute.magitech.mana_efficiency", 1, 0, Double.MAX_VALUE).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> EMBER_SPELL_POWER = ATTRIBUTES.register("ember_spell_power", () -> (new PercentageAttribute("attribute.magitech.ember_spell_power", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> GLACE_SPELL_POWER = ATTRIBUTES.register("glace_spell_power", () -> (new PercentageAttribute("attribute.magitech.glace_spell_power", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> SURGE_SPELL_POWER = ATTRIBUTES.register("surge_spell_power", () -> (new PercentageAttribute("attribute.magitech.surge_spell_power", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> PHANTOM_SPELL_POWER = ATTRIBUTES.register("phantom_spell_power", () -> (new PercentageAttribute("attribute.magitech.phantom_spell_power", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> TREMOR_SPELL_POWER = ATTRIBUTES.register("tremor_spell_power", () -> (new PercentageAttribute("attribute.magitech.tremor_spell_power", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> MAGIC_SPELL_POWER = ATTRIBUTES.register("magic_spell_power", () -> (new PercentageAttribute("attribute.magitech.magic_spell_power", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> FLOW_SPELL_POWER = ATTRIBUTES.register("flow_spell_power", () -> (new PercentageAttribute("attribute.magitech.flow_spell_power", 1, 0, Double.MAX_VALUE).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> HOLLOW_SPELL_POWER = ATTRIBUTES.register("hollow_spell_power", () -> (new PercentageAttribute("attribute.magitech.hollow_spell_power", 1, 0, Double.MAX_VALUE).setSyncable(true)));

    public static void registerEntityAttributes(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    @SubscribeEvent
    public static void modifyAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> event.add(entity, attribute)));
    }
}