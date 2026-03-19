package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.MagitechRegistries;

import java.awt.*;

public abstract class ToolProperty<T> implements IToolProperty<T> {
    Color color;

    public ToolProperty(Color color) {
        this.color = color;
    }

    public MutableComponent getDisplayName() {
        ResourceLocation key = MagitechRegistries.TOOL_PROPERTY.getKey(this);
        return Component.translatable("tool." + key.getPath() + ".property." + key.getNamespace());
    }
}