package net.stln.magitech.content.item.tool.partitem;


import net.stln.magitech.feature.tool.ToolPart;
import net.stln.magitech.feature.tool.material.ToolMaterial;

public class PlateItem extends PartItem {
    protected ToolMaterial material;

    public PlateItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.PLATE;
    }
}
