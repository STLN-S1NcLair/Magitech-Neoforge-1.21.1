package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.property.ToolPropertyLike;
import org.jetbrains.annotations.NotNull;

public class DeferredToolProperty<T extends IToolProperty<?>> extends DeferredHolder<IToolProperty<?>, T> implements ToolPropertyLike {
    public DeferredToolProperty(ResourceKey<IToolProperty<?>> key) {
        super(key);
    }

    public DeferredToolProperty(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.TOOL_PROPERTY, id));
    }

    @Override
    public @NotNull IToolProperty<?> asToolProperty() {
        return get();
    }
}
