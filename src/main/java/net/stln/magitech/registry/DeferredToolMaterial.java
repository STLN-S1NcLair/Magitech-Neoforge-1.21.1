package net.stln.magitech.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.material.ToolMaterialLike;
import org.jetbrains.annotations.NotNull;

public class DeferredToolMaterial<T extends ToolMaterial> extends DeferredHolder<ToolMaterial, T> implements ToolMaterialLike {
    public DeferredToolMaterial(ResourceKey<ToolMaterial> key) {
        super(key);
    }
    
    public DeferredToolMaterial(ResourceLocation id) {
        this(ResourceKey.create(MagitechRegistries.Keys.TOOL_MATERIAL, id));
    }

    @Override
    public @NotNull ToolMaterial asToolMaterial() {
        return get();
    }
}
