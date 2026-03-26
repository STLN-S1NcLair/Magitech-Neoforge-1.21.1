package net.stln.magitech.feature.tool.property;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.helper.MathHelper;

import java.awt.*;
import java.util.List;

public abstract class AttributeToolProperty<T> extends ToolProperty<T> implements CalculableToolProperty<T> {
    Holder<Attribute> attribute;

    public AttributeToolProperty(Holder<Attribute> attribute, ToolPropertyCategory group) {
        super(group);
        this.attribute = attribute;
    }

    public AttributeToolProperty(Holder<Attribute> attribute, Color color) {
        super(color);
        this.attribute = attribute;
    }

    public Holder<Attribute> getAttribute(ToolProperties properties) {
        return attribute;
    }

    public double apply(ToolProperties properties) {
        if (!properties.getValues().containsKey(this)) return 0.0;

        Attribute attribute = this.getAttribute(properties).value();
        double defaultVal = attribute.getDefaultValue();
        // デフォルト値を差し引いて適用
        return this.scalarValue(properties.get(this)) - defaultVal;
    }

    @Override
    public void addPartTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(Component.literal(String.valueOf(MathHelper.round(properties.getScalar(this), 2))).append("x")
                        .withColor(getColor().getRGB())));
    }
}