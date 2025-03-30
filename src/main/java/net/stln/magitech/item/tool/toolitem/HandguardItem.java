package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.ToolPart;

public class HandguardItem extends PartItem {
    protected ToolMaterial material;

    public HandguardItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.HANDGUARD;
    }
}
