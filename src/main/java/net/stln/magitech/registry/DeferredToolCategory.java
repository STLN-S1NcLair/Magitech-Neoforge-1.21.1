package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.tool_category.ToolCategory;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryLike;
import org.jetbrains.annotations.NotNull;

public class DeferredToolCategory<T extends ToolCategory> extends DeferredHolder<ToolCategory, T> implements ToolCategoryLike {
    public DeferredToolCategory(ResourceKey<ToolCategory> key) {
        super(key);
    }

    public DeferredToolCategory(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.TOOL_CATEGORY, id));
    }

    @Override
    public @NotNull ToolCategory asToolGroup() {
        return get();
    }
}
