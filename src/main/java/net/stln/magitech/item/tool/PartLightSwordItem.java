package net.stln.magitech.item.tool;

public class PartLightSwordItem extends PartToolItem {
    public PartLightSwordItem(Properties settings) {
        super(settings);
    }

    @Override
    public ToolType getToolType() {
        return ToolType.LIGHT_SWORD;
    }
}
