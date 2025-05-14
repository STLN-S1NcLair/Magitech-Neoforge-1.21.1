package net.stln.magitech.item.tool.partitem;

import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class HeavyBladeItem extends PartItem {
    protected ToolMaterial material;

    public HeavyBladeItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.HEAVY_BLADE;
    }
}
