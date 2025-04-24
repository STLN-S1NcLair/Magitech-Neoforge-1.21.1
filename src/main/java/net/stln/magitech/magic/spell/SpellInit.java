package net.stln.magitech.magic.spell;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.spell.ember.Fluvalen;
import net.stln.magitech.magic.spell.glace.Cryoluxa;
import net.stln.magitech.magic.spell.glace.Frigala;
import net.stln.magitech.magic.spell.magic.Arcaleth;
import net.stln.magitech.magic.spell.phantom.Mirazien;
import net.stln.magitech.magic.spell.surge.Sparkion;
import net.stln.magitech.magic.spell.tremor.Ocsilbeam;

public class SpellInit {

    public static void registerSpells() {
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluvalen"), new Fluvalen());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "cryoluxa"), new Cryoluxa());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "frigala"), new Frigala());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "sparkion"), new Sparkion());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "oscilbeam"), new Ocsilbeam());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mirazien"), new Mirazien());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "arcaleth"), new Arcaleth());
    }
}
