package net.stln.magitech.feature.tool.property;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.awt.*;

public abstract class InitialToolProperty<T> extends ToolProperty<T> implements CalculableToolProperty<T> {

    public InitialToolProperty(Color color) {
        super(color);
    }
}