package net.stln.magitech.feature.tool.property;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class PointToolProperty extends InitialIntegerToolProperty {

    Supplier<DataComponentType<Integer>> component;

    public PointToolProperty(float order, Supplier<DataComponentType<Integer>> component, ToolPropertyCategory group) {
        super(order, group);
        this.component = component;
    }

    public PointToolProperty(float order, Supplier<DataComponentType<Integer>> component, Color color) {
        super(order, color);
        this.component = component;
    }

    @Override
    public void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(Component.literal(String.valueOf(stack.getOrDefault(component, 0))).withColor(getColor().getRGB())));
    }

    @Override
    public void addRationalTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        addTooltip(stack, properties, components);
    }
}