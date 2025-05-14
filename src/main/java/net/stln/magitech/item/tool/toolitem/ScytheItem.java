package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;

public class ScytheItem extends PartToolItem {
    public ScytheItem(Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.SCYTHE;
    }

    public float getMultiplier(ToolPart part) {
        return switch (part) {
            case TOOL_BINDING -> 0.2F;
            case HEAVY_HANDLE -> 1.9F;
            case HEAVY_BLADE -> 0.8F;
            case REINFORCED_STICK -> 1.1F;
            default -> 1F;
        } / getToolType().getSize();
    }
}
