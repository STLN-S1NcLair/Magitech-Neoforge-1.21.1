package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public interface IToolProperty<T> extends ToolPropertyLike<T> {

    float order();

    ToolPropertyCategory getCategory();

    MutableComponent getDisplayName();

    Color getColor();

    @Override
    default @NotNull IToolProperty<T> asToolProperty() {
        return this;
    }

    void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components);

    // デフォルト値の何倍かを+N%形式で表示、杖に使用
    void addRationalTooltip(ItemStack stack, ToolProperties properties, List<Component> components);

    void addPartTooltip(ItemStack stack, ToolProperties properties, List<Component> components);
}