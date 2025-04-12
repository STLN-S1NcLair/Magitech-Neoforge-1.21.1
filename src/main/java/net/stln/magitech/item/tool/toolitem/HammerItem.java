package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;

public class HammerItem extends PartToolItem {
    public HammerItem(Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.HAMMER;
    }

    public float getMultiplier(ToolPart part) {
        return switch (part) {
            case TOOL_BINDING -> 0.3F;
            case PLATE -> 1.2F;
            case STRIKE_HEAD -> 1.7F;
            case HEAVY_HANDLE -> 0.8F;
            default -> 1F;
        } / getToolType().getSize();
    }
}
