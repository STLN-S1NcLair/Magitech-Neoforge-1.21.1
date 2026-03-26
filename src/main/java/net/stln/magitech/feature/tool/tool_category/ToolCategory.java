package net.stln.magitech.feature.tool.tool_category;

import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public record ToolCategory(List<ToolPropertyLike<?>> keys) implements ToolCategoryLike {

    public ToolProperties cast(ToolProperties properties) {
        Map<IToolProperty<?>, Object> map = properties.getValues();
        for (IToolProperty<?> key : map.keySet()) {
            if (!keys.contains(key)) {
                map.remove(key);
            }
        }
        return new ToolProperties(this, map);
    }

    @Override
    public @NotNull ToolCategory asToolGroup() {
        return this;
    }
}
