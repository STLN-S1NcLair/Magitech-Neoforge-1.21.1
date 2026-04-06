package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.helper.MathHelper;

import java.awt.*;
import java.util.List;

public class DoubleToolProperty extends ToolProperty<Double> implements CalculableToolProperty<Double> {

    public DoubleToolProperty(float order, ToolPropertyCategory group) {
        super(order, group);
    }

    public DoubleToolProperty(float order, Color color) {
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
    public void addTooltip(ItemStack stack, ToolProperties properties, List<net.minecraft.network.chat.Component> components) {
        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(Component.literal(String.valueOf(MathHelper.round(properties.getScalar(this), 2)))
                        .withColor(getColor().getRGB())));
    }

    @Override
    public void addRationalTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        int value = Math.round(properties.getScalar(this) * 100 - 100);
        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(Component.literal((value >= 0 ? "+" : "") + value + "%")
                        .withColor(getColor().getRGB())));
    }

    @Override
    public void addPartTooltip(ItemStack stack, ToolProperties properties, List<net.minecraft.network.chat.Component> components) {
    }
}