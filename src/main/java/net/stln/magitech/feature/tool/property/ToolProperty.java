package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.helper.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public abstract class ToolProperty<T> implements IToolProperty<T> {
    protected ToolPropertyCategory group;
    protected Color defaultColor = Color.WHITE;

    public ToolProperty(@Nullable ToolPropertyCategory group) {
        this.group = group;
    }

    public ToolProperty(Color color) {
        this.group = null;
        this.defaultColor = color;
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
        return Component.translatable("tool." + key.getPath() + ".property." + key.getNamespace());
    }
}