package net.stln.magitech.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TooltipTextSignItem extends SignItem {

    public TooltipTextSignItem(Properties properties, Block standingBlock, Block wallBlock) {
        super(properties, standingBlock, wallBlock);
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.block." + stack.getItem().toString().replace(":", ".")).withColor(0x808080));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
