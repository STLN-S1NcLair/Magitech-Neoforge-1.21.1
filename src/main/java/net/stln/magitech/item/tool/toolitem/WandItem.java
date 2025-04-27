package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;

public class WandItem extends SpellCasterItem {
    public WandItem(Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.WAND;
    }

    public float getMultiplier(ToolPart part) {
        return switch (part) {
            case TOOL_BINDING -> 0.6F;
            case CONDUCTOR -> 1.3F;
            case LIGHT_HANDLE -> 0.5F;
            case CATALYST -> 1.6F;
            default -> 1F;
        } / getToolType().getSize();
    }
}
