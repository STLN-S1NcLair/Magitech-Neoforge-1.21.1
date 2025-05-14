package net.stln.magitech.item.tool.partitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class ConductorItem extends PartItem {
    protected ToolMaterial material;

    public ConductorItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.CONDUCTOR;
    }
}
