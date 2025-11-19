package net.stln.magitech;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.stln.magitech.item.fluid.FluidContainerMatcher;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.magic.spell.Spell;

public final class MagitechRegistries {
    public static final Registry<Spell> SPELL = new RegistryBuilder<>(Keys.SPELL).sync(true).create();
    public static final Registry<ToolMaterial> TOOL_MATERIAL = new RegistryBuilder<>(Keys.TOOL_MATERIAL).sync(true).create();
    public static final Registry<FluidContainerMatcher> FLUID_CONTAINER_MATCHER = new RegistryBuilder<>(Keys.FLUID_CONTAINER_MATCHER).sync(true).create();
    private MagitechRegistries() {
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener((NewRegistryEvent event) -> {
            event.register(SPELL);
            event.register(TOOL_MATERIAL);
            event.register(FLUID_CONTAINER_MATCHER);
        });
        Magitech.LOGGER.info("Registering Spells for" + Magitech.MOD_ID);
    }

    public static final class Keys {
        public static final ResourceKey<? extends Registry<Spell>> SPELL = ResourceKey.createRegistryKey(Magitech.id("spell"));
        public static final ResourceKey<? extends Registry<ToolMaterial>> TOOL_MATERIAL = ResourceKey.createRegistryKey(Magitech.id("tool_material"));
        public static final ResourceKey<? extends Registry<FluidContainerMatcher>> FLUID_CONTAINER_MATCHER = ResourceKey.createRegistryKey(Magitech.id("alchemical_flask_containable"));
        private Keys() {
        }
    }
}
