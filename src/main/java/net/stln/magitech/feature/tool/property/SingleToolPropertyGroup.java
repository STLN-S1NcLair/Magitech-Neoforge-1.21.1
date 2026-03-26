package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.MutableComponent;

import java.util.Set;

public record SingleToolPropertyGroup(IToolProperty<?> property) implements IToolPropertyGroup {
    @Override
    public boolean contains(IToolProperty<?> property) {
        return this.property == property;
    }

    @Override
    public MutableComponent getDisplayText() {
        return property.getDisplayName().withColor(property.getColor().getRGB());
    }
}
