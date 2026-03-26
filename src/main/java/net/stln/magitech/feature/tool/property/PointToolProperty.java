package net.stln.magitech.feature.tool.property;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.content.item.component.PartMaterialComponent;
import net.stln.magitech.helper.ColorHelper;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.RenderHelper;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class PointToolProperty extends InitialIntegerToolProperty {

    Supplier<DataComponentType<Integer>> component;

    public PointToolProperty(Supplier<DataComponentType<Integer>> component, ToolPropertyCategory group) {
        super(group);
        this.component = component;
    }

    public PointToolProperty(Supplier<DataComponentType<Integer>> component, Color color) {
        super(color);
        this.component = component;
    }

    @Override
    public void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(Component.literal(String.valueOf(stack.getOrDefault(component, 0))).withColor(getColor().getRGB())));
    }
}