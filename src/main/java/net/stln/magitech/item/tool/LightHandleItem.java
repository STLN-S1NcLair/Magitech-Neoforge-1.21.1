package net.stln.magitech.item.tool;


public class LightHandleItem extends PartItem {
    protected ToolMaterial material;

    public LightHandleItem(Properties settings) {
        super(settings);
    }

    public ToolPart getPart() {
        return ToolPart.LIGHT_HANDLE;
    }
}
