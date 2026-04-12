package net.stln.magitech.content.item.tooltip_item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.content.block.ManaContainerBlock;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.helper.ManaContainerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TooltipTextManaContainerBlockItem extends TooltipTextBlockItem {
    public TooltipTextManaContainerBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        int titleColor = 0x5a9e91;
        int color = 0xcdffde;
        ManaContainerBlock block = (ManaContainerBlock) getBlock();
        ManaContainerHelper.addManaContainerBlockItemInfo(block.getMaxMana(), block.getMaxFlow(), tooltipComponents, color, titleColor);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
