package net.stln.magitech.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class TooltipTextPlaceableItem extends BlockItem {
    public TooltipTextPlaceableItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item." + stack.getItem().toString().replace(":", ".")).withColor(0x808080));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }


    @Override
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }
}
