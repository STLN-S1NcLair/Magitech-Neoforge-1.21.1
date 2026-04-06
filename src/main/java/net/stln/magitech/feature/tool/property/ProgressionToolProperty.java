package net.stln.magitech.feature.tool.property;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.helper.ColorHelper;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.RenderHelper;

import java.awt.*;
import java.util.List;

public class ProgressionToolProperty extends InitialIntegerToolProperty {

    public ProgressionToolProperty(float order, ToolPropertyCategory group) {
        super(order, group);
    }

    public ProgressionToolProperty(float order) {
        super(order, Color.WHITE);
    }

    @Override
    public void addTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        int tier = ComponentHelper.getTier(stack);
        int tierColor = ColorHelper.getTierColor(tier);
        int progression = stack.getOrDefault(ComponentInit.PROGRESSION_COMPONENT, 0);
        int maxProgression = stack.getOrDefault(ComponentInit.MAX_PROGRESSION_COMPONENT, 0);

        // ゲージ
        components.add(RenderHelper.getGradationGauge(0, maxProgression, progression, 30, tierColor, ColorHelper.getTierColor(tier + 1)));
        // 数値
        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(Component.literal(String.valueOf(progression)).withColor(0xFFFFFF)).append(" / ").append(Component.literal(String.valueOf(maxProgression))
                        .withColor(tierColor)));
    }

    @Override
    public void addRationalTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        addTooltip(stack, properties, components);
    }

    @Override
    public void addPartTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
    }
}