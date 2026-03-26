package net.stln.magitech.feature.tool.tool_type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record ToolMineType(Set<MineType> types) {

    public static ToolMineType create(MineType... type) {
        return new ToolMineType(Set.of(type));
    }

    public static ToolMineType none() {
        return new ToolMineType(Set.of());
    }
}
