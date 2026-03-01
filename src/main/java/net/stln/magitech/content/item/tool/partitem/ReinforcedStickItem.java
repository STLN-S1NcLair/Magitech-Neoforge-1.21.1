package net.stln.magitech.content.item.tool.partitem;


import net.stln.magitech.feature.tool.ToolPart;
import net.stln.magitech.feature.tool.material.ToolMaterial;

public class ReinforcedStickItem extends PartItem {
    protected ToolMaterial material;

    public ReinforcedStickItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.REINFORCED_STICK;
    }
}
