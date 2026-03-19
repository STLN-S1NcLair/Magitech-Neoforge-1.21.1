package net.stln.magitech.content.item.tool.partitem;


import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.material.ToolMaterial;

public class HeavyHandleItem extends PartItem {
    protected ToolMaterial material;

    public HeavyHandleItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.HEAVY_HANDLE;
    }
}
