package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class HeavyHandleItem extends PartItem {
    protected ToolMaterial material;

    public HeavyHandleItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.HEAVY_HANDLE;
    }
}
