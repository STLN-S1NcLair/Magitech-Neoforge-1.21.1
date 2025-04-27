package net.stln.magitech.item.tool.toolitem;

import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class LightBladeItem extends PartItem {
    protected ToolMaterial material;

    public LightBladeItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.LIGHT_BLADE;
    }
}
