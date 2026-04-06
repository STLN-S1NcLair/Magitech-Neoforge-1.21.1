package net.stln.magitech.feature.tool.upgrade;

import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.feature.tool.tool_category.ToolCategory;
import net.stln.magitech.feature.tool.tool_category.ToolCategoryLike;

import java.util.Collections;
import java.util.List;

public class SingleUpgrade extends SimpleUpgrade {

    public SingleUpgrade(ToolPropertyModifier mod, ToolCategoryLike category) {
        super(Collections.singletonList(mod), category);
    }
}
