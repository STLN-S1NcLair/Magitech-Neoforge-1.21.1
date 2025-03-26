package net.stln.magitech.damage;

import net.minecraft.world.entity.Entity;
import net.stln.magitech.item.tool.Element;

import java.util.HashMap;
import java.util.Map;

public class EntityElementDictionary {
    private static final Map<Class<? extends Entity>, Element> dict = new HashMap<>();

    public static ElementAffinity getElementAffinity(Entity target, Element element) {
        Element targetElement = dict.getOrDefault(target.getClass(), Element.NONE);
        return ElementAffinityDictionary.getElementAffinity(element, targetElement);
    }

    public static void registerEntityElement(Class<? extends Entity> entity, Element element) {
        dict.put(entity, element);
    }
}
