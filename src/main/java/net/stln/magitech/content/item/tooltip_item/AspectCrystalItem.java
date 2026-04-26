package net.stln.magitech.content.item.tooltip_item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.feature.element.Element;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AspectCrystalItem extends TooltipTextItem {

    Element element;

    public AspectCrystalItem(Properties properties, Element element) {
        super(properties);
        this.element = element;
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.item." + stack.getItem().toString().replace(":", ".") + ".aspect").withColor(element.getTextColor().darker().darker().getRGB()));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
