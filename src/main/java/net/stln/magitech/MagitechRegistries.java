package net.stln.magitech;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.stln.magitech.content.item.fluid.FluidContainerMatcher;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluenceType;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.tool_category.ToolCategory;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.trait.Trait;
import net.stln.magitech.feature.tool.upgrade.Upgrade;

@EventBusSubscriber
public final class MagitechRegistries {
    public static final Registry<ISpell> SPELL = create(Keys.SPELL);
    public static final Registry<ToolPart> TOOL_PART = create(Keys.TOOL_PART);
    public static final Registry<ToolCategory> TOOL_CATEGORY = create(Keys.TOOL_CATEGORY);
    public static final Registry<ToolType> TOOL_TYPE = create(Keys.TOOL_TYPE);
    public static final Registry<IToolProperty<?>> TOOL_PROPERTY = create(Keys.TOOL_PROPERTY);
    public static final Registry<ToolMaterial> TOOL_MATERIAL = create(Keys.TOOL_MATERIAL);
    public static final Registry<Trait> TRAIT = create(Keys.TRAIT);
    public static final Registry<Upgrade> UPGRADE = create(Keys.UPGRADE);
    public static final Registry<FieldInfluenceType> FIELD_INFLUENCE_TYPE = create(Keys.FIELD_INFLUENCE_TYPE);
    public static final Registry<FieldEffectType> FIELD_EFFECT_TYPE = create(Keys.FIELD_EFFECT_TYPE);
    public static final Registry<FluidContainerMatcher> FLUID_CONTAINER_MATCHER = create(Keys.FLUID_CONTAINER_MATCHER);

    private MagitechRegistries() {
    }

    private static <T> Registry<T> create(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).sync(true).create();
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(SPELL);
        event.register(TOOL_PART);
        event.register(TOOL_CATEGORY);
        event.register(TOOL_TYPE);
        event.register(TOOL_PROPERTY);
        event.register(TOOL_MATERIAL);
        event.register(TRAIT);
        event.register(UPGRADE);
        event.register(FIELD_INFLUENCE_TYPE);
        event.register(FIELD_EFFECT_TYPE);
        event.register(FLUID_CONTAINER_MATCHER);
        Magitech.LOGGER.info("Registering Registries for" + Magitech.MOD_ID);
    }

    public static final class Keys {
        public static final ResourceKey<Registry<ISpell>> SPELL = create("spell");
        public static final ResourceKey<Registry<ToolPart>> TOOL_PART = create("tool_part");
        public static final ResourceKey<Registry<ToolCategory>> TOOL_CATEGORY = create("tool_category");
        public static final ResourceKey<Registry<ToolType>> TOOL_TYPE = create("tool_type");
        public static final ResourceKey<Registry<IToolProperty<?>>> TOOL_PROPERTY = create("tool_property");
        public static final ResourceKey<Registry<ToolMaterial>> TOOL_MATERIAL = create("tool_material");
        public static final ResourceKey<Registry<Trait>> TRAIT = create("trait");
        public static final ResourceKey<Registry<Upgrade>> UPGRADE = create("upgrade");
        public static final ResourceKey<Registry<FieldInfluenceType>> FIELD_INFLUENCE_TYPE = create("field_influence_type");
        public static final ResourceKey<Registry<FieldEffectType>> FIELD_EFFECT_TYPE = create("field_effect_type");
        public static final ResourceKey<Registry<FluidContainerMatcher>> FLUID_CONTAINER_MATCHER = create("alchemical_flask_containable");

        private Keys() {
        }

        private static <T> ResourceKey<Registry<T>> create(String path) {
            return ResourceKey.createRegistryKey(Magitech.id(path));
        }
    }
}
