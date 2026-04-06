package net.stln.magitech.feature.tool.property;

import java.awt.*;

public abstract class InitialIntegerToolProperty extends InitialToolProperty<Integer> {

    public InitialIntegerToolProperty(float order, ToolPropertyCategory group) {
        super(order, group);
    }

    public InitialIntegerToolProperty(float order, Color color) {
        super(order, color);
    }

    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer mul(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer scalarAdd(Integer a, float b) {
        return (int) (a + b);
    }

    @Override
    public Integer scalarMul(Integer a, float b) {
        return (int) (a * b);
    }

    @Override
    public float scalarValue(Integer a) {
        return a;
    }

    @Override
    public Integer identity() {
        return 0;
    }
}