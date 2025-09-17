package net.stln.magitech.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.util.ClientHelper;
import net.stln.magitech.util.ColorHelper;
import net.stln.magitech.util.ComponentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ThreadPageItem extends TooltipTextItem {

    public ThreadPageItem(Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return ComponentHelper.getThreadPageSpell(stack)
                .map(spell -> Component.translatable("item.magitech.thread_page", spell.getDescription()))
                .orElseGet(() -> super.getName(stack).copy());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        ComponentHelper.getThreadPageSpell(stack).ifPresent(spell -> {
            tooltipComponents.add(spell.getDescription().withColor(spell.getElement().getSpellColor()));
            Player player = ClientHelper.getPlayer();
            if (player == null) return;
            List<Component> componentList = spell.getTooltip(player.level(), player, stack);
            int i = 0;
            for (Component component : componentList) {
                int col = component.getStyle().getColor() != null ? component.getStyle().getColor().getValue() : 0x808080;
                int newCol = ColorHelper.Argb.getArgb(ColorHelper.Argb.getRed(col) / 2, ColorHelper.Argb.getGreen(col) / 2, ColorHelper.Argb.getBlue(col) / 2);
                componentList.set(i, Component.empty().append(((MutableComponent) component).withColor(newCol)));
                i++;
            }
            tooltipComponents.addAll(componentList);
        });
    }
}
