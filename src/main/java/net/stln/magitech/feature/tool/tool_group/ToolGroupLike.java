package net.stln.magitech.feature.tool.tool_group;

import net.stln.magitech.feature.tool.part.ToolPart;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ToolGroupLike {
    @NotNull ToolGroup asToolGroup();
}
