package net.stln.magitech.content.item.tool.partitem;


import net.stln.magitech.feature.tool.ToolPart;
import net.stln.magitech.feature.tool.material.ToolMaterial;

public class StrikeHeadItem extends PartItem {
    protected ToolMaterial material;

    public StrikeHeadItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.STRIKE_HEAD;
    }
}
