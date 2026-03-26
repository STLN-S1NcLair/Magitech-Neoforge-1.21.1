package net.stln.magitech.feature.tool.tool_type;

import net.stln.magitech.feature.tool.part.ToolPartLike;
import net.stln.magitech.feature.tool.property.ToolProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ToolType(ToolMineType mineType, ToolProperties defaultProperties, List<PartData> parts) implements ToolTypeLike {
    @Override
    public @NotNull ToolType asToolType() {
        return this;
    }

    public record PartData(ToolPartLike part, float weight) {

    }
}
