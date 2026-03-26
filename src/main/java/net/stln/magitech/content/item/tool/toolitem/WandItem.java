package net.stln.magitech.content.item.tool.toolitem;


import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.tool_type.ToolTypeInit;

public class WandItem extends SpellCasterItem {
    public WandItem(Properties settings) {
        super(settings, ToolTypeInit.WAND);
    }
}
