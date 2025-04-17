package net.stln.magitech.magic.spell;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.spell.ember.Ashveil;
import net.stln.magitech.magic.spell.glace.Frozbeam;
import net.stln.magitech.magic.spell.magic.Arcether;
import net.stln.magitech.magic.spell.surge.Stormhaze;

public class SpellInit {

    public static void registerSpells() {
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "arcether"), new Arcether());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "stormhaze"), new Stormhaze());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "frozbeam"), new Frozbeam());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ashveil"), new Ashveil());
    }
}
