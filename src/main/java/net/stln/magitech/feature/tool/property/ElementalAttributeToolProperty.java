package net.stln.magitech.feature.tool.property;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.MathHelper;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementalAttributeToolProperty extends AttributeToolProperty<Map<Element, Double>> {

    public ElementalAttributeToolProperty(float order, Holder<Attribute> attribute, ToolPropertyCategory group) {
        super(order, attribute, group);
    }

    public ElementalAttributeToolProperty(float order, Holder<Attribute> attribute, Color color) {
        super(order, attribute, color);
    }

    public static Map<Element, Double> flatValue(double value) {
        Map<Element, Double> result = new HashMap<>();
        for (Element element : Element.values()) {
            result.put(element, value);
        }
        return result;
    }

    public static Map<Element, Double> none() {
        Map<Element, Double> result = new HashMap<>();
        for (Element elm : Element.values()) {
            result.put(elm, scalarIdentity());
        }
        return result;
    }

    public static Map<Element, Double> singleElement(Element element, double value) {
        Map<Element, Double> result = new HashMap<>();
        for (Element elm : Element.values()) {
            if (element == elm) {
                result.put(elm, value);
            } else {
                result.put(elm, scalarIdentity());
            }
        }
        return result;
    }

    @Override
    public Map<Element, Double> add(Map<Element, Double> a, Map<Element, Double> b) {
        Map<Element, Double> result = new HashMap<>();
        for (Element element : Element.values()) {
            result.put(element, a.getOrDefault(element, scalarIdentity()) + b.getOrDefault(element, scalarIdentity()));
        }
        return result;
    }

    @Override
    public Map<Element, Double> mul(Map<Element, Double> a, Map<Element, Double> b) {
        Map<Element, Double> result = new HashMap<>();
        for (Element element : Element.values()) {
            result.put(element, a.getOrDefault(element, scalarIdentity()) * b.getOrDefault(element, scalarIdentity()));
        }
        return result;
    }

    @Override
    public Map<Element, Double> scalarAdd(Map<Element, Double> a, float b) {
        Map<Element, Double> result = new HashMap<>();
        for (Element element : Element.values()) {
            result.put(element, a.getOrDefault(element, scalarIdentity()) + b);
        }
        return result;
    }

    @Override
    public Map<Element, Double> scalarMul(Map<Element, Double> a, float b) {
        Map<Element, Double> result = new HashMap<>();
        for (Element element : Element.values()) {
            result.put(element, a.getOrDefault(element, scalarIdentity()) * b);
        }
        return result;
    }

    @Override
    public float scalarValue(Map<Element, Double> a) {
        Element maxElement = Element.NONE;
        double max = Double.MIN_VALUE;
        for (Element element : Element.values()) {
            double current = a.getOrDefault(element, scalarIdentity());
            if (max < current) {
                maxElement = element;
                max = current;
            }
        }
        float result = 0.0F;
        for (Element element : Element.values()) {
            result += (float) (a.getOrDefault(element, scalarIdentity()) * (element == maxElement ? 1.0F : 0.75F));
        }
        return result;
    }

    @Override
    public Map<Element, Double> identity() {
        return new HashMap<>();
    }

    public static double scalarIdentity() {
        return 0.0;
    }

    public Element getElement(Map<Element, Double> a) {
        if (a == null) return Element.NONE;
        Element maxElement = Element.NONE;
        double max = Double.MIN_VALUE;
        for (Element element : Element.values()) {
            double current = a.getOrDefault(element, scalarIdentity());
            if (max < current) {
                maxElement = element;
                max = current;
            }
        }
        return maxElement;
    }

    @Override
    public void addTooltip(ItemStack stack, ToolProperties properties, List<net.minecraft.network.chat.Component> components) {
        Element element = this.getElement(properties.getOrId(this));

        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(element.getDisplayName().append(" ").append(String.valueOf(MathHelper.round(properties.getScalar(this), 2)))
                        .withColor(element.getTextColor().getRGB())));
    }

    @Override
    public void addRationalTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        Element element = this.getElement(properties.getOrId(this));

        int value = Math.round(properties.getScalar(this) * 100);
        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(element.getDisplayName().append(" ").append(Component.literal((value >= 0 ? "+" : "") + value + "%"))
                        .withColor(element.getTextColor().getRGB())));
    }

    @Override
    public void addPartTooltip(ItemStack stack, ToolProperties properties, List<Component> components) {
        Element element = this.getElement(properties.getOrId(this));

        components.add(ToolPropertyHelper.getToolTipComponent(this)
                .append(element.getDisplayName().append(" ").append(Component.literal(String.valueOf(MathHelper.round(properties.getScalar(this), 2))).append("x"))
                        .withColor(element.getTextColor().getRGB())));
    }
}
