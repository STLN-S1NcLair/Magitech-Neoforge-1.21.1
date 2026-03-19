package net.stln.magitech.content.item.tool.partitem;


import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.material.ToolMaterial;

public class ConductorItem extends PartItem {
    protected ToolMaterial material;

    public ConductorItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.CONDUCTOR;
    }
}
