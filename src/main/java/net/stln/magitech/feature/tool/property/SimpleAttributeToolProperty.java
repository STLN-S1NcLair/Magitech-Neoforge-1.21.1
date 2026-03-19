package net.stln.magitech.feature.tool.property;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.awt.*;

public class SimpleAttributeToolProperty extends AttributeToolProperty<Double> {

    public SimpleAttributeToolProperty(Holder<Attribute> attribute, Color color) {
        super(attribute, color);
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
    public Double addIdentity() {
        return 0.0;
    }

    @Override
    public Double mulIdentity() {
        return 1.0;
    }
}