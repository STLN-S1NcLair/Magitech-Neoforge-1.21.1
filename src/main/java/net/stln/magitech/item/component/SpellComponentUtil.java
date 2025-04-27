package net.stln.magitech.item.component;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;

import java.util.ArrayList;
import java.util.List;

public class SpellComponentUtil {
    public static SpellComponent generateFromId(List<String> ids, int selected) {
        List<Spell> spells = new ArrayList<>();
        for (String id : ids) {
            if (id != null) {
                spells.add(SpellRegister.getSpell(ResourceLocation.parse(id)));
            }
        }
        return new SpellComponent(spells, selected);
    }

    public static SpellComponent generateFromId(List<String> ids) {
        List<Spell> spells = new ArrayList<>();
        for (String id : ids) {
            if (id != null) {
                spells.add(SpellRegister.getSpell(ResourceLocation.parse(id)));
            }
        }
        return new SpellComponent(spells, 0);
    }
}
