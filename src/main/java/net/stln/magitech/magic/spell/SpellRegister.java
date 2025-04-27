package net.stln.magitech.magic.spell;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class SpellRegister {
    private static Map<ResourceLocation, Spell> register = new HashMap<>();

    public static void registerSpell(ResourceLocation resourceLocation, Spell spell) {
        register.put(resourceLocation, spell);
    }

    public static Spell getSpell(ResourceLocation resourceLocation) {
        return register.get(resourceLocation);
    }

    public static Spell getSpellFromString(String resourceLocation) {
        return register.get(ResourceLocation.parse(resourceLocation));
    }

    public static ResourceLocation getId(Spell spell) {
        if (spell != null) {
            for (Map.Entry<ResourceLocation, Spell> entry : register.entrySet()) {
                if (entry.getValue().getClass().equals(spell.getClass())) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static String getStringId(Spell spell) {
        if (spell != null) {
        return getId(spell).toString();
        }
        return "";
    }
}
