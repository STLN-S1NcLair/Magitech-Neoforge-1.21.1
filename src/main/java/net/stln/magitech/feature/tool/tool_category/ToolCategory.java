package net.stln.magitech.feature.tool.tool_category;

import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyLike;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public record ToolCategory(Supplier<List<ToolPropertyLike<?>>> keys) implements ToolCategoryLike {

    public ToolProperties cast(ToolProperties properties) {
        Map<IToolProperty<?>, Object> map = new HashMap<>(properties.getValues());
        for (IToolProperty<?> key : new HashSet<>(map.keySet())) {
            if (!getProps().contains(key)) {
                map.remove(key);
            }
        }
        return new ToolProperties(this, map);
    }

    public List<ToolPropertyLike<?>> getKeys() {
        return keys.get();
    }

    public List<? extends IToolProperty<?>> getProps() {
        return getKeys().stream().map(ToolPropertyLike::asToolProperty).toList();
    }

    @Override
    public @NotNull ToolCategory asToolCategory() {
        return this;
    }
}
