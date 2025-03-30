package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.ToolPart;

public class LightHandleItem extends PartItem {
    protected ToolMaterial material;

    public LightHandleItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.LIGHT_HANDLE;
    }
}
