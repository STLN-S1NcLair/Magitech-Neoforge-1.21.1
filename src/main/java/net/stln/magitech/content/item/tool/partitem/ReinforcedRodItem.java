package net.stln.magitech.content.item.tool.partitem;


import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.material.ToolMaterial;

public class ReinforcedRodItem extends PartItem {
    protected ToolMaterial material;

    public ReinforcedRodItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.REINFORCED_ROD;
    }
}
