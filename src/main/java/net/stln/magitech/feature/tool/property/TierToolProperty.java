package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.helper.ColorHelper;
import net.stln.magitech.helper.ComponentHelper;

import java.awt.*;
import java.util.List;

public class TierToolProperty extends InitialDoubleToolProperty {

    public TierToolProperty(float order, ToolPropertyCategory group) {
        super(order, group);
    }

    public TierToolProperty(float order) {
        super(order, Color.WHITE);
    }

    @Override
    public void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        int tier = ComponentHelper.getTier(stack);
        int tierColor = ColorHelper.getTierColor(tier);

        components.add(this.getDisplayName().append(" ")
                .append(String.valueOf(tier)).withColor(tierColor));
    }

    @Override
    public void addRationalTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        addTooltip(stack, properties, components);
    }

    @Override
    public void addPartTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        int tier = ComponentHelper.getTier(stack);
        int tierColor = ColorHelper.getTierColor(tier);

        components.add(this.getDisplayName().append(" ")
                .append(String.valueOf(tier)).withColor(tierColor));
    }
}