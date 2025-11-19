package net.stln.magitech.item.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.item.TooltipTextItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ToolBeltItem extends TooltipTextItem {
    public ToolBeltItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
//        tooltipComponents.add(Component.translatable("tooltip.hint.item.magitech.aggregated_fluxia").withColor(0xC0F0FF));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
