package net.stln.magitech.feature.tool.property;

import net.stln.magitech.feature.tool.tool_category.ToolCategory;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryLike;

import java.util.HashMap;
import java.util.Map;

public class ToolProperties {
    private final ToolCategoryLike group;
    private final Map<IToolProperty<?>, Object> values;

    public ToolProperties(ToolCategoryLike group, Map<IToolProperty<?>, Object> values) {
        this.group = group;
        this.values = values;
    }

    public ToolProperties(ToolCategoryLike group) {
        this(group, new HashMap<>());
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
        if (!getGroup().keys().contains(prop)) {
            return null;
        }
        return (T) values.get(prop.asToolProperty());
    }

    @SuppressWarnings("unchecked")
    public <T> float getScalar(CalculableToolProperty<T> prop) {
        if (!getGroup().keys().contains(prop)) {
            return 0.0F;
        }
        return prop.scalarValue((T) values.get(prop.asToolProperty()));
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(ToolPropertyLike<T> prop, T val) {
        return (T) values.getOrDefault(prop.asToolProperty(), val);
    }

    public ToolProperties copy() {
        return new ToolProperties(getGroup(), new HashMap<>(values));
    }

    public ToolCategory getGroup() {
        return group.asToolGroup();
    }

    public Map<IToolProperty<?>, Object> getValues() {
        return values;
    }
}
