package net.stln.magitech.feature.tool.tool_category;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ToolCategoryLike {
    @NotNull ToolCategory asToolGroup();
}
