package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public interface IToolProperty<T> extends ToolPropertyLike<T> {

    ToolPropertyCategory getCategory();

    MutableComponent getDisplayName();

    Color getColor();

    @Override
    default @NotNull IToolProperty<T> asToolProperty() {
        return this;
    }

    void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components);

    void addPartTooltip(ItemStack stack, ToolProperties properties, List<Component> components);
}