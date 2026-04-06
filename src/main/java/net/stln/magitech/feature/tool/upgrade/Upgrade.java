package net.stln.magitech.feature.tool.upgrade;

import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.feature.tool.tool_category.ToolCategory;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryLike;

import java.util.List;

public abstract class Upgrade {

    public abstract List<ToolPropertyModifier> getModifiers(int level);

    public abstract boolean applicable(ToolCategoryLike category);
}
