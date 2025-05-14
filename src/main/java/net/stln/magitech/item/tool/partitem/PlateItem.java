package net.stln.magitech.item.tool.partitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class PlateItem extends PartItem {
    protected ToolMaterial material;

    public PlateItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.PLATE;
    }
}
