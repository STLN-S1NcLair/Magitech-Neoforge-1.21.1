package net.stln.magitech.item.tool.partitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class HandguardItem extends PartItem {
    protected ToolMaterial material;

    public HandguardItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.HANDGUARD;
    }
}
