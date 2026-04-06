package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.MutableComponent;

import java.awt.*;

public record SingleToolPropertyGroup(IToolProperty<?> property) implements IToolPropertyGroup {
    @Override
    public boolean contains(IToolProperty<?> property) {
        return this.property == property;
    }

    @Override
    public Color getColor() {
        return property.getColor();
    }

    @Override
    public MutableComponent getDisplayText() {
        return property.getDisplayName().withColor(getColor().getRGB());
    }
}
