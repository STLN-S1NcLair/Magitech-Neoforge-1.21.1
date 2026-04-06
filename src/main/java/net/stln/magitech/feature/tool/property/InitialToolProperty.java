package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.helper.MathHelper;

import java.awt.*;
import java.util.List;

public abstract class InitialToolProperty<T> extends ToolProperty<T> implements CalculableToolProperty<T> {

    public InitialToolProperty(float order, ToolPropertyCategory group) {
        super(order, group);
    }

    public InitialToolProperty(float order, Color color) {
        super(order, color);
    }

    @Override
    public void addPartTooltip(ItemStack stack, ToolProperties properties, List<net.minecraft.network.chat.Component> components) {
        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(Component.literal(String.valueOf(MathHelper.round(properties.getScalar(this), 2))).append("x")
                        .withColor(getColor().getRGB())));
    }
}