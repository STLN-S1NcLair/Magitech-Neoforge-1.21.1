package net.stln.magitech.item.tool.material;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ToolMaterialLike {
    @NotNull ToolMaterial asToolMaterial();
}
