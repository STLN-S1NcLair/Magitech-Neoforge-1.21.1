package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.MagitechRegistries;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public abstract class ToolProperty<T> implements IToolProperty<T> {
    protected ToolPropertyCategory group;
    protected Color defaultColor = Color.WHITE;

    private float order;

    public ToolProperty(float order, @Nullable ToolPropertyCategory group) {
        this.group = group;
        this.order = order;
    }

    public ToolProperty(float order, Color color) {
        this.order = order;
        this.group = null;
        this.defaultColor = color;
    }

    @Override
    public float order() {
        return order;
    }

    @Override
    public ToolPropertyCategory getCategory() {
        return group;
    }

    @Override
    public Color getColor() {
        return group != null ? group.getColor() : defaultColor;
    }

    public MutableComponent getDisplayName() {
        ResourceLocation key = MagitechRegistries.TOOL_PROPERTY.getKey(this);
        return Component.translatable("tool." + key.getNamespace() + ".property." + key.getPath());
    }
}