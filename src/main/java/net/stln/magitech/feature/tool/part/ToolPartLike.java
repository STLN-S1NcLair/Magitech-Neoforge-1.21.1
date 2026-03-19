package net.stln.magitech.feature.tool.part;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ToolPartLike {
    @NotNull ToolPart asToolPart();
}
