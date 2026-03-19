package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.tool_type.ToolTypeLike;
import org.jetbrains.annotations.NotNull;

public class DeferredToolType<T extends ToolType> extends DeferredHolder<ToolType, T> implements ToolTypeLike {
    public DeferredToolType(ResourceKey<ToolType> key) {
        super(key);
    }

    public DeferredToolType(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.TOOL_TYPE, id));
    }

    @Override
    public @NotNull ToolType asToolType() {
        return get();
    }
}
