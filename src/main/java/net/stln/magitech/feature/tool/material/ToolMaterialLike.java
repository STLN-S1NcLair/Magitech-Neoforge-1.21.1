package net.stln.magitech.feature.tool.material;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ToolMaterialLike {
    @NotNull ToolMaterial asToolMaterial();
}
