package net.stln.magitech.damage;

import net.stln.magitech.util.Element;
import net.stln.magitech.util.Map2d;

import java.util.ArrayList;
import java.util.List;

public class ElementAffinityRegister {
    private static final Map2d<Element, Boolean, List<Element>> dict = new Map2d<>();
    public static boolean EFFICIENT = true;
    public static boolean INEFFICIENT = false;

    public static ElementAffinity getElementAffinity(Element element, Element targetElement) {
        if (dict.containsKey1(element)) {
            if (dict.get(element, EFFICIENT).contains(targetElement)) {
                return ElementAffinity.EFFICIENT;
            }
            if (dict.get(element, INEFFICIENT).contains(targetElement)) {
                return ElementAffinity.INEFFICIENT;
            }
        }
        return ElementAffinity.NORMAL;
    }

    public static void registerAffinity(Element element, boolean efficiency, Element target) {
        List<Element> list = dict.getOrDefault(element, efficiency, new ArrayList<>());
        list.add(target);
        dict.put(element, efficiency, list);
    }

}
