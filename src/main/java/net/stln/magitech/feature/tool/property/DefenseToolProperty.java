package net.stln.magitech.feature.tool.property;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.helper.MathHelper;

import java.awt.*;
import java.util.List;

public class DefenseToolProperty extends SimpleAttributeToolProperty {

    public DefenseToolProperty(float order, Holder<Attribute> attribute, ToolPropertyCategory group) {
        super(order, attribute, group);
    }

    public DefenseToolProperty(float order, Holder<Attribute> attribute, Color color) {
        super(order, attribute, color);
    }

    @Override
    public boolean isRational(ItemStack stack) {
        return false;
    }
}