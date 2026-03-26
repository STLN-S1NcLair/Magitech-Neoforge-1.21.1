package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.helper.ColorHelper;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.RenderHelper;

import java.awt.*;
import java.util.List;

public class TierToolProperty extends InitialIntegerToolProperty {

    public TierToolProperty(ToolPropertyCategory group) {
        super(group);
    }

    public TierToolProperty() {
        super(Color.WHITE);
    }

    @Override
    public void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        int tier = ComponentHelper.getTier(stack);
        int tierColor = ColorHelper.getTierColor(tier);

        components.add(this.getDisplayName().append(" ")
                .append(String.valueOf(tier)).withColor(tierColor));
    }

    @Override
    public void addPartTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        int tier = ComponentHelper.getTier(stack);
        int tierColor = ColorHelper.getTierColor(tier);

        components.add(this.getDisplayName().append(" ")
                .append(String.valueOf(tier)).withColor(tierColor));
    }
}