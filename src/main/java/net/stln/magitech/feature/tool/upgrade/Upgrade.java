package net.stln.magitech.feature.tool.upgrade;

import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;

import java.util.List;

public abstract class Upgrade {

    public abstract List<ToolPropertyModifier> getModifiers(int level);
}
