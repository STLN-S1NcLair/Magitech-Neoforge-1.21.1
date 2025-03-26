package net.stln.magitech.item.tool;


public class HandguardItem extends PartItem {
    protected ToolMaterial material;

    public HandguardItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.HANDGUARD;
    }
}
