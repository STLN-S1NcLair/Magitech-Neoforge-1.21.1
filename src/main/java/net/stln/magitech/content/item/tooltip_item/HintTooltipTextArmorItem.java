package net.stln.magitech.content.item.tooltip_item;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.content.item.armor.TooltipArmorItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HintTooltipTextArmorItem extends TooltipArmorItem {
    public HintTooltipTextArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.hint.item." + stack.getItem().toString().replace(":", ".")).withColor(0xC0C0C0));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
