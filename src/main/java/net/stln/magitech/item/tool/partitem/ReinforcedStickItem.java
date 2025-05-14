package net.stln.magitech.item.tool.partitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class ReinforcedStickItem extends PartItem {
    protected ToolMaterial material;

    public ReinforcedStickItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.REINFORCED_STICK;
    }
}
