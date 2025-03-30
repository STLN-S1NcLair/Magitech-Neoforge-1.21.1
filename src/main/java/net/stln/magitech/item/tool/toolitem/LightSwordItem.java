package net.stln.magitech.item.tool.toolitem;


import net.minecraft.world.item.Item;
import net.stln.magitech.item.tool.ToolType;

public class LightSwordItem extends PartToolItem {
    public LightSwordItem(Item.Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.LIGHT_SWORD;
    }
}
