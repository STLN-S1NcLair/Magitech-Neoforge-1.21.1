package net.stln.magitech.item.energy;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.api.ManaCapabilities;
import net.stln.magitech.api.mana.handler.IBasicManaHandler;
import net.stln.magitech.item.TooltipTextItem;
import net.stln.magitech.util.ManaContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ManaContainerItem extends TooltipTextItem {

    public ManaContainerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        int titleColor = 0x5a9e91;
        int color = 0xcdffde;
        IBasicManaHandler handler = stack.getCapability(ManaCapabilities.MANA_CONTAINER_ITEM);
        ManaContainerUtil.addManaContainerItemInfo(handler.getMana(), handler.getMaxMana(), handler.getMaxFlow(), tooltipComponents, color, titleColor);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getCapability(ManaCapabilities.MANA_CONTAINER_ITEM).getMana() > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        IBasicManaHandler manaHandler = stack.getCapability(ManaCapabilities.MANA_CONTAINER_ITEM);
        return (int) Math.round(13.0 * manaHandler.fillRatio());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float fillRatio = (float) stack.getCapability(ManaCapabilities.MANA_CONTAINER_ITEM).fillRatio();
        float h = Mth.lerp(fillRatio, 188, 133) / 360;
        float s = Mth.lerp(fillRatio, 66, 37) / 100;
        float v = Mth.lerp(fillRatio, 36, 100) / 100;
        return Mth.hsvToRgb(h, s, v);
    }
}
