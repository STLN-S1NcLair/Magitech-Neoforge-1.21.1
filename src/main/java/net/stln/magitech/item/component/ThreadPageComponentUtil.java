package net.stln.magitech.item.component;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;

import java.util.ArrayList;
import java.util.List;

public class ThreadPageComponentUtil {
    public static ThreadPageComponent generateFromId(String id) {
        return new ThreadPageComponent(SpellRegister.getSpell(ResourceLocation.parse(id)));
    }
}
