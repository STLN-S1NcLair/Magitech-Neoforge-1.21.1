package net.stln.magitech.entity.status;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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

    public static void registerEntityAttributes(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    @SubscribeEvent
    public static void modifyAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> event.add(entity, attribute)));
    }
}