package net.stln.magitech.content.item.tooltip_item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.content.block.ManaContainerBlock;
import net.stln.magitech.core.api.mana.flow.network.connectable.IManaWirelessWaypoint;
import net.stln.magitech.helper.EnergyFormatter;
import net.stln.magitech.helper.ManaContainerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TooltipTextManaWirelessConnectorBlockItem extends TooltipTextBlockItem {
    public TooltipTextManaWirelessConnectorBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        int titleColor = 0x5a9e91;
        int color = 0xcdffde;
        IManaWirelessWaypoint block = (IManaWirelessWaypoint) getBlock();
        tooltipComponents.add(Component.translatable("gui.magitech.max_connection_range").append(": ").withColor(titleColor).append(Component.literal(String.valueOf(block.getRange())).withColor(color)));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
