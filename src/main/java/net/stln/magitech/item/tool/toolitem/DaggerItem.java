package net.stln.magitech.item.tool.toolitem;


import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;

public class DaggerItem extends PartToolItem {
    public DaggerItem(Properties settings) {
        super(settings);
    }

    public ToolType getToolType() {
        return ToolType.DAGGER;
    }

    public float getMultiplier(ToolPart part) {
        return switch (part) {
            case HANDGUARD -> 1.1F;
            case LIGHT_BLADE -> 1.5F;
            case LIGHT_HANDLE -> 0.4F;
            default -> 1F;
        } / getToolType().getSize();
    }
}
