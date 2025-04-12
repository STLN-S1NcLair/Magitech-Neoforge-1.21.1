package net.stln.magitech.item.tool.toolitem;


import net.minecraft.world.item.Item;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;

public class LightSwordItem extends PartToolItem {
    public LightSwordItem(Item.Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.LIGHT_SWORD;
    }

    public float getMultiplier(ToolPart part) {
        return switch (part) {
            case TOOL_BINDING -> 0.3F;
            case HANDGUARD -> 1.3F;
            case LIGHT_BLADE -> 1.5F;
            case LIGHT_HANDLE -> 0.9F;
            default -> 1F;
        } / getToolType().getSize();
    }
}
