package net.stln.magitech.item.tool;


import net.minecraft.world.item.Item;

public class LightSwordItem extends PartToolItem {
    public LightSwordItem(Item.Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.LIGHT_SWORD;
    }
}
