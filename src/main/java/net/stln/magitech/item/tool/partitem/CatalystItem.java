package net.stln.magitech.item.tool.partitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class CatalystItem extends PartItem {
    protected ToolMaterial material;

    public CatalystItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.CATALYST;
    }
}
