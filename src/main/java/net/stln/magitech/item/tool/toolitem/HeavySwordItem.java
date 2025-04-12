package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;

public class HeavySwordItem extends PartToolItem {
    public HeavySwordItem(Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.HEAVY_SWORD;
    }

    public float getMultiplier(ToolPart part) {
        return switch (part) {
            case TOOL_BINDING -> 0.3F;
            case HANDGUARD -> 1.1F;
            case HEAVY_BLADE -> 1.7F;
            case LIGHT_HANDLE -> 0.9F;
            default -> 1F;
        } / getToolType().getSize();
    }
}
