package net.stln.magitech;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.stln.magitech.content.item.fluid.FluidContainerMatcher;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.tool_group.ToolGroup;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.part.ToolPart;

public final class MagitechRegistries {
    public static final Registry<ISpell> SPELL = new RegistryBuilder<>(Keys.SPELL).sync(true).create();
    public static final Registry<ToolPart> TOOL_PART = new RegistryBuilder<>(Keys.TOOL_PART).sync(true).create();
    public static final Registry<ToolGroup> TOOL_GROUP = new RegistryBuilder<>(Keys.TOOL_GROUP).sync(true).create();
    public static final Registry<ToolType> TOOL_TYPE = new RegistryBuilder<>(Keys.TOOL_TYPE).sync(true).create();
    public static final Registry<IToolProperty<?>> TOOL_PROPERTY = new RegistryBuilder<>(Keys.TOOL_PROPERTY).sync(true).create();
    public static final Registry<ToolMaterial> TOOL_MATERIAL = new RegistryBuilder<>(Keys.TOOL_MATERIAL).sync(true).create();
    public static final Registry<FluidContainerMatcher> FLUID_CONTAINER_MATCHER = new RegistryBuilder<>(Keys.FLUID_CONTAINER_MATCHER).sync(true).create();

    private MagitechRegistries() {
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener((NewRegistryEvent event) -> {
            event.register(SPELL);
            event.register(TOOL_PART);
            event.register(TOOL_GROUP);
            event.register(TOOL_TYPE);
            event.register(TOOL_PROPERTY);
            event.register(TOOL_MATERIAL);
            event.register(FLUID_CONTAINER_MATCHER);
        });
        Magitech.LOGGER.info("Registering Spells for" + Magitech.MOD_ID);
    }

    public static final class Keys {
        public static final ResourceKey<? extends Registry<ISpell>> SPELL = ResourceKey.createRegistryKey(Magitech.id("spell"));
        public static final ResourceKey<? extends Registry<ToolPart>> TOOL_PART = ResourceKey.createRegistryKey(Magitech.id("tool_part"));
        public static final ResourceKey<? extends Registry<ToolGroup>> TOOL_GROUP = ResourceKey.createRegistryKey(Magitech.id("tool_group"));
        public static final ResourceKey<? extends Registry<ToolType>> TOOL_TYPE = ResourceKey.createRegistryKey(Magitech.id("tool_type"));
        public static final ResourceKey<? extends Registry<IToolProperty<?>>> TOOL_PROPERTY = ResourceKey.createRegistryKey(Magitech.id("tool_property"));
        public static final ResourceKey<? extends Registry<ToolMaterial>> TOOL_MATERIAL = ResourceKey.createRegistryKey(Magitech.id("tool_material"));
        public static final ResourceKey<? extends Registry<FluidContainerMatcher>> FLUID_CONTAINER_MATCHER = ResourceKey.createRegistryKey(Magitech.id("alchemical_flask_containable"));

        private Keys() {
        }
    }
}
