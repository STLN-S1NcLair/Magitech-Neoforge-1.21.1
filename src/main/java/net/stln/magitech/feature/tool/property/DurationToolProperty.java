package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;

public class DurationToolProperty extends InitialDoubleToolProperty {

    public DurationToolProperty(float order, ToolPropertyCategory group) {
        super(order, group);
    }

    public DurationToolProperty(float order) {
        super(order, Color.WHITE);
    }

    @Override
    public void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        int maxDuration = stack.getMaxDamage() - 1;
        int duration = maxDuration - stack.getDamageValue();

        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(Component.literal(duration + " / " + maxDuration)
                        .withColor(getColor().getRGB())));
    }

    @Override
    public void addRationalTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        addTooltip(stack, properties, components);
    }
}