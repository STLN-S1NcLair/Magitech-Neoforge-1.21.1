package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.material.ToolMaterial;

public class ToolBindingItem extends PartItem {
    protected ToolMaterial material;

    public ToolBindingItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.TOOL_BINDING;
    }
}
