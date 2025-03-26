package net.stln.magitech.item.tool;


import net.minecraft.world.item.Item;

public abstract class PartItem extends Item {
    public PartItem(Properties settings) {
        super(settings);
    }

    public abstract ToolPart getPart();
}
