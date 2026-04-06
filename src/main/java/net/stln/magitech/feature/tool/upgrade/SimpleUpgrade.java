package net.stln.magitech.feature.tool.upgrade;

import net.stln.magitech.feature.tool.property.IToolProperty;
import net.stln.magitech.feature.tool.property.ToolPropertyLike;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryLike;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleUpgrade extends Upgrade {

    List<ToolPropertyModifier> mods;
    ToolCategoryLike category;

    public SimpleUpgrade(List<ToolPropertyModifier> mods, ToolCategoryLike category) {
        this.mods = mods;
        this.category = category;
    }

    @Override
    public List<ToolPropertyModifier> getModifiers(int level) {
        return mods;
    }

    @Override
    public boolean applicable(ToolCategoryLike category) {
        Set<IToolProperty<?>> set = this.category.asToolCategory().getKeys().stream().map(ToolPropertyLike::asToolProperty).collect(Collectors.toSet());
        return category.asToolCategory().getKeys().stream().allMatch(propertyLike -> set.contains(propertyLike.asToolProperty()));
    }
}
