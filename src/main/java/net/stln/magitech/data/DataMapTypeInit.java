package net.stln.magitech.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.stln.magitech.Magitech;

public class DataMapTypeInit {
    public static final DataMapType<EntityType<?>, EntityElementData> ENTITY_ELEMENT = DataMapType.builder(Magitech.id("element"), Registries.ENTITY_TYPE, EntityElementData.CODEC)
            .synced(EntityElementData.CODEC, false)
            .build();

    public static void registerDataMapTypes(IEventBus bus) {
        Magitech.LOGGER.info("Registering Data Map Types for" + Magitech.MOD_ID);
        bus.addListener((RegisterDataMapTypesEvent event) -> {
            event.register(ENTITY_ELEMENT);
        });
    }
}
