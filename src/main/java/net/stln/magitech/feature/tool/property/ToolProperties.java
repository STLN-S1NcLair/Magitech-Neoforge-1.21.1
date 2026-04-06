package net.stln.magitech.feature.tool.property;

import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.feature.tool.tool_category.ToolCategory;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryInit;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryLike;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ToolProperties {
    private final ToolCategoryLike category;
    private final Map<IToolProperty<?>, Object> values;

    public ToolProperties(ToolCategoryLike category, Map<IToolProperty<?>, Object> values) {
        this.category = category;
        this.values = values;
    }

    public ToolProperties(ToolCategoryLike group) {
        this(group, new HashMap<>());
    }

    public <T> ToolProperties set(ToolPropertyLike<T> prop, T value) {
        if (!getCategory().getKeys().stream().anyMatch(propertyLike ->  prop.asToolProperty() == propertyLike.asToolProperty())) {
            throw new IllegalArgumentException("this ToolGroup does not have such property!");
        }
        values.put(prop.asToolProperty(), value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ToolPropertyLike<T> prop) {
        if (!getCategory().getProps().contains(prop)) {
            return null;
        }
        return (T) values.get(prop);
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrId(CalculableToolProperty<T> prop) {
        if (!getCategory().getProps().contains(prop)) {
            return prop.identity();
        }
        return (T) values.get(prop);
    }

    @SuppressWarnings("unchecked")
    public <T> float getScalar(CalculableToolProperty<T> prop) {
        if (!getCategory().getProps().contains(prop)) {
            return 0.0F;
        }
        return prop.scalarValue((T) values.get(prop));
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(ToolPropertyLike<T> prop, T val) {
        return (T) values.getOrDefault(prop.asToolProperty(), val);
    }

    public ToolProperties copy() {
        return new ToolProperties(getCategory(), new HashMap<>(values));
    }

    public ToolCategory getCategory() {
        return category.asToolCategory();
    }

    public Map<IToolProperty<?>, Object> getValues() {
        return values;
    }
}
