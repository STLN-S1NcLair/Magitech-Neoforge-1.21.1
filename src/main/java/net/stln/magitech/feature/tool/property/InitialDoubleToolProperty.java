package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;

public abstract class InitialDoubleToolProperty extends InitialToolProperty<Double> {

    public InitialDoubleToolProperty(float order, ToolPropertyCategory group) {
        super(order, group);
    }

    public InitialDoubleToolProperty(float order, Color color) {
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
}