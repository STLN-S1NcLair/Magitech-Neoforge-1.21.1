package net.stln.magitech.feature.tool.property;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.stln.magitech.feature.element.Element;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ElementalAttributeToolProperty extends AttributeToolProperty<Map<Element, Double>> {

    public ElementalAttributeToolProperty(Holder<Attribute> attribute, Color color) {
        super(attribute, color);
    }

    public static Map<Element, Double> flatValue(double value) {
        Map<Element, Double> result = new HashMap<>();
        for (Element element : Element.values()) {
            result.put(element, value);
        }
        return result;
    }

    public static Map<Element, Double> singleElement(Element element, double value) {
        Map<Element, Double> result = new HashMap<>();
        for (Element elm : Element.values()) {
            if (element == elm) {
                result.put(elm, value);
            } else {
                result.put(elm, 0.0);
            }
        }
        return result;
    }

    @Override
    public Map<Element, Double> add(Map<Element, Double> a, Map<Element, Double> b) {
        Map<Element, Double> result = new HashMap<>();
        for (Element element : Element.values()) {
            result.put(element, a.getOrDefault(element, 0.0) + b.getOrDefault(element, 0.0));
        }
        return result;
    }

    @Override
    public Map<Element, Double> mul(Map<Element, Double> a, Map<Element, Double> b) {
        Map<Element, Double> result = new HashMap<>();
        for (Element element : Element.values()) {
            result.put(element, a.getOrDefault(element, 1.0) * b.getOrDefault(element, 1.0));
        }
        return result;
    }

    @Override
    public Map<Element, Double> addIdentity() {
        return new HashMap<>();
    }

    @Override
    public Map<Element, Double> mulIdentity() {
        return new HashMap<>();
    }
}
