package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolType;

public class HammerItem extends PartToolItem {
    public HammerItem(Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.HAMMER;
    }
}
