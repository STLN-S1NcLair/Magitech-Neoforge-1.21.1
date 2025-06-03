package net.stln.magitech.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;
import net.stln.magitech.util.ColorHelper;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadPageItem extends TooltipTextItem {

    public ThreadPageItem(Properties settings) {
        super(settings);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(ComponentInit.THREAD_PAGE_COMPONENT)) {
            Spell spell = stack.get(ComponentInit.THREAD_PAGE_COMPONENT).spell();
            return Component.translatable("item.magitech.thread_page", Component.translatable("spell.magitech." + SpellRegister.getId(stack.get(ComponentInit.THREAD_PAGE_COMPONENT).getSpell()).getPath()));
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(ComponentInit.THREAD_PAGE_COMPONENT)) {
            Spell spell = stack.get(ComponentInit.THREAD_PAGE_COMPONENT).spell();
                ResourceLocation location = SpellRegister.getId(spell);
                if (location != null) {
                            tooltipComponents.add(Component.translatable("spell." + location.getNamespace() + "." + location.getPath()).withColor(spell.getElement().getColor()));
                        }
            List<Component> componentList = spell.getTooltip(Minecraft.getInstance().level, Minecraft.getInstance().player, stack);
                int i = 0;
                for (Component component : componentList) {
                    int col = component.getStyle().getColor() != null ? component.getStyle().getColor().getValue() : 0x808080;
                    int newCol = ColorHelper.Argb.getArgb(ColorHelper.Argb.getRed(col) / 2, ColorHelper.Argb.getGreen(col) / 2, ColorHelper.Argb.getBlue(col) / 2);
                    componentList.set(i, Component.empty().append(((MutableComponent) component).withColor(newCol)));
                    i++;
                }
            tooltipComponents.addAll(componentList);
                }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
