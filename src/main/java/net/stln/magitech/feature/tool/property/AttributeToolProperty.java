package net.stln.magitech.feature.tool.property;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.awt.*;

public abstract class AttributeToolProperty<T> extends ToolProperty<T> implements CalculableToolProperty<T> {
    Holder<Attribute> attribute;

    public AttributeToolProperty(Holder<Attribute> attribute, Color color) {
        super(color);
        this.attribute = attribute;
    }
}