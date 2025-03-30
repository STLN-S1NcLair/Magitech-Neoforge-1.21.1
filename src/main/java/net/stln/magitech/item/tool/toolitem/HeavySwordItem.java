package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolType;

public class HeavySwordItem extends PartToolItem {
    public HeavySwordItem(Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.HEAVY_SWORD;
    }
}
