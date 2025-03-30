package net.stln.magitech.item.tool.toolitem;


import net.minecraft.world.item.Item;
import net.stln.magitech.item.tool.ToolPart;

public abstract class PartItem extends Item {
    public PartItem(Properties settings) {
        super(settings);
    }

    public abstract ToolPart getPart();
}
