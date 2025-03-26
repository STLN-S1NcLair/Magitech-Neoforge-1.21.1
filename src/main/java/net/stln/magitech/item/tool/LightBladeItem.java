package net.stln.magitech.item.tool;

public class LightBladeItem extends PartItem {
    protected ToolMaterial material;

    public LightBladeItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.LIGHT_BLADE;
    }
}
