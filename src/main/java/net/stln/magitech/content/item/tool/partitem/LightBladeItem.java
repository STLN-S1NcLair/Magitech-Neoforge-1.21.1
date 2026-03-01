package net.stln.magitech.content.item.tool.partitem;

import net.stln.magitech.feature.tool.ToolPart;
import net.stln.magitech.feature.tool.material.ToolMaterial;

public class LightBladeItem extends PartItem {
    protected ToolMaterial material;

    public LightBladeItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.LIGHT_BLADE;
    }
}
