package net.stln.magitech.content.item.tool.toolitem;


import net.minecraft.world.item.Item;
import net.stln.magitech.feature.tool.tool_type.ToolTypeInit;

public class LightSwordItem extends SynthesisedToolItem {
    public LightSwordItem(Item.Properties settings) {
        super(settings, ToolTypeInit.LIGHT_SWORD);
    }
}
