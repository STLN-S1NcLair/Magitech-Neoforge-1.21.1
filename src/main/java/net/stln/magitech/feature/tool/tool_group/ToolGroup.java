package net.stln.magitech.feature.tool.tool_group;

import net.stln.magitech.feature.tool.property.ToolProperty;
import net.stln.magitech.feature.tool.property.ToolPropertyLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ToolGroup(List<ToolPropertyLike<?>> keys) implements ToolGroupLike {
    @Override
    public @NotNull ToolGroup asToolGroup() {
        return this;
    }
}
