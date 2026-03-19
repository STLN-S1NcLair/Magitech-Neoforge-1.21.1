package net.stln.magitech.feature.tool.property;

import net.stln.magitech.feature.tool.tool_group.ToolGroup;
import net.stln.magitech.feature.tool.tool_group.ToolGroupLike;

import java.util.HashMap;
import java.util.Map;

public class ToolProperties {
    private final ToolGroupLike group;
    private final Map<IToolProperty<?>, Object> values;

    public ToolProperties(ToolGroupLike group) {
        this.group = group;
        this.values = new HashMap<>();
    }

    public <T> ToolProperties set(ToolPropertyLike<T> prop, T value) {
        if (!getGroup().keys().contains(prop)) {
            throw new IllegalArgumentException("this ToolGroup does not have such property!");
        }
        values.put(prop.asToolProperty(), value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ToolPropertyLike<T> prop) {
        return (T) values.get(prop.asToolProperty());
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(ToolPropertyLike<T> prop, T val) {
        return (T) values.getOrDefault(prop.asToolProperty(), val);
    }

    public ToolGroup getGroup() {
        return group.asToolGroup();
    }
}
