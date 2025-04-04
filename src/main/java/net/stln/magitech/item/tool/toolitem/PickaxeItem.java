package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolType;

public class PickaxeItem extends PartToolItem {
    public PickaxeItem(Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }
}
