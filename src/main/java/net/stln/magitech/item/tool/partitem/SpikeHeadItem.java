package net.stln.magitech.item.tool.partitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class SpikeHeadItem extends PartItem {
    protected ToolMaterial material;

    public SpikeHeadItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.SPIKE_HEAD;
    }
}
