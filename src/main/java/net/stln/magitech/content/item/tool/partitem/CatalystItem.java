package net.stln.magitech.content.item.tool.partitem;


import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.material.ToolMaterial;

public class CatalystItem extends PartItem {
    protected ToolMaterial material;

    public CatalystItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.CATALYST;
    }
}
