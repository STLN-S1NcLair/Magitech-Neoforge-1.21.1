package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;

public class PickaxeItem extends PartToolItem {
    public PickaxeItem(Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.PICKAXE;
    }

    public float getMultiplier(ToolPart part) {
        return switch (part) {
            case TOOL_BINDING -> 0.5F;
            case SPIKE_HEAD -> 1.7F;
            case HEAVY_HANDLE -> 0.8F;
            default -> 1F;
        } / getToolType().getSize();
    }
}
