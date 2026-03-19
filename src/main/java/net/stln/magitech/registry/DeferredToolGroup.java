package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.tool_group.ToolGroup;
import net.stln.magitech.feature.tool.tool_group.ToolGroupLike;
import org.jetbrains.annotations.NotNull;

public class DeferredToolGroup<T extends ToolGroup> extends DeferredHolder<ToolGroup, T> implements ToolGroupLike {
    public DeferredToolGroup(ResourceKey<ToolGroup> key) {
        super(key);
    }

    public DeferredToolGroup(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.TOOL_GROUP, id));
    }

    @Override
    public @NotNull ToolGroup asToolGroup() {
        return get();
    }
}
