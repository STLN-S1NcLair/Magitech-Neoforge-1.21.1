package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.part.ToolPartLike;
import org.jetbrains.annotations.NotNull;

public class DeferredToolPart<T extends ToolPart> extends DeferredHolder<ToolPart, T> implements ToolPartLike {
    public DeferredToolPart(ResourceKey<ToolPart> key) {
        super(key);
    }

    public DeferredToolPart(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.TOOL_PART, id));
    }

    @Override
    public @NotNull ToolPart asToolPart() {
        return get();
    }
}
