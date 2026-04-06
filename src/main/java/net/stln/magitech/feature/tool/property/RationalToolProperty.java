package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.helper.MathHelper;

import java.awt.*;
import java.util.List;

public class RationalToolProperty extends DoubleToolProperty {

    public RationalToolProperty(float order, ToolPropertyCategory group) {
        super(order, group);
    }

    public RationalToolProperty(float order, Color color) {
        super(order, color);
    }

    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double mul(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double scalarAdd(Double a, float b) {
        return a + b;
    }

    @Override
    public Double scalarMul(Double a, float b) {
        return a * b;
    }

    @Override
    public float scalarValue(Double a) {
        return a.floatValue();
    }

    @Override
    public Double identity() {
        return 0.0;
    }

    @Override
    public void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        addRationalTooltip(stack, properties, components);
    }

    @Override
    public void addPartTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
    }
}