package net.stln.magitech.feature.tool.property;

import java.awt.*;

public class InitialIntegerToolProperty extends InitialToolProperty<Integer> {

    public InitialIntegerToolProperty(Color color) {
        super(color);
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
    public Integer addIdentity() {
        return 0;
    }

    @Override
    public Integer mulIdentity() {
        return 1;
    }
}