package net.stln.magitech.feature.tool.tool_type;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ToolTypeLike {
    @NotNull ToolType asToolType();
}
