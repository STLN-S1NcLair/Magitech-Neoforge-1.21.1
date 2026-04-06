package net.stln.magitech.feature.tool.property;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.stln.magitech.content.entity.status.AttributeInit;

import java.awt.*;

public class SpellElementalAttributeToolProperty extends ElementalAttributeToolProperty {

    public SpellElementalAttributeToolProperty(float order, ToolPropertyCategory group) {
        super(order, null, group);
    }

    public SpellElementalAttributeToolProperty(float order, Color color) {
        super(order, null, color);
    }

    @Override
    public Holder<Attribute> getAttribute(ToolProperties properties) {
        return getElement(properties.getOrId(this)).getPowerAttribute().orElse(AttributeInit.SPELL_POWER);
    }

    @Override
    protected double getDefaultValue(AttributeSupplier build, Holder<Attribute> holder, Attribute attribute) {
        return 0.0;
    }
}
