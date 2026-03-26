package net.stln.magitech.feature.tool.property;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.content.entity.status.AttributeInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.MathHelper;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellElementalAttributeToolProperty extends ElementalAttributeToolProperty {

    public SpellElementalAttributeToolProperty(ToolPropertyCategory group) {
        super(null, group);
    }

    public SpellElementalAttributeToolProperty(Color color) {
        super(null, color);
    }

    @Override
    public Holder<Attribute> getAttribute(ToolProperties properties) {
        return getElement(properties.get(this)).getPowerAttribute().orElse(AttributeInit.SPELL_POWER);
    }
}
