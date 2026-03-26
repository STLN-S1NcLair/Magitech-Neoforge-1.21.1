package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.helper.ColorHelper;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.RenderHelper;

import java.awt.*;
import java.util.List;

public class DurationToolProperty extends InitialIntegerToolProperty {

    public DurationToolProperty(ToolPropertyCategory group) {
        super(group);
    }

    public DurationToolProperty() {
        super(Color.WHITE);
    }

    @Override
    public void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        int maxDuration = stack.getMaxDamage() - 1;
        int duration = maxDuration - stack.getDamageValue();

        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(Component.literal(duration + " / " + maxDuration)
                        .withColor(getColor().getRGB())));
    }
}