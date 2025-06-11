package net.stln.magitech.magic.spell;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.spell.ember.Fluvalen;
import net.stln.magitech.magic.spell.ember.Ignisca;
import net.stln.magitech.magic.spell.ember.Pyrolux;
import net.stln.magitech.magic.spell.flow.Aeltherin;
import net.stln.magitech.magic.spell.flow.Fluvinae;
import net.stln.magitech.magic.spell.flow.Mistrelune;
import net.stln.magitech.magic.spell.glace.Cryoluxa;
import net.stln.magitech.magic.spell.glace.Frigala;
import net.stln.magitech.magic.spell.glace.Nivalune;
import net.stln.magitech.magic.spell.hollow.Disparundra;
import net.stln.magitech.magic.spell.hollow.Nullixis;
import net.stln.magitech.magic.spell.hollow.Tenebrisol;
import net.stln.magitech.magic.spell.hollow.Voidlance;
import net.stln.magitech.magic.spell.magic.Arcaleth;
import net.stln.magitech.magic.spell.magic.Glymora;
import net.stln.magitech.magic.spell.magic.Mystaven;
import net.stln.magitech.magic.spell.mana.Enercrux;
import net.stln.magitech.magic.spell.phantom.Mirazien;
import net.stln.magitech.magic.spell.phantom.Phantastra;
import net.stln.magitech.magic.spell.phantom.Veilmist;
import net.stln.magitech.magic.spell.surge.Fulgenza;
import net.stln.magitech.magic.spell.surge.Sparkion;
import net.stln.magitech.magic.spell.surge.Voltaris;
import net.stln.magitech.magic.spell.tremor.Oscilbeam;
import net.stln.magitech.magic.spell.tremor.Sonistorm;
import net.stln.magitech.magic.spell.tremor.Tremivox;

public class SpellInit {

    public static void registerSpells() {
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ignisca"), new Ignisca());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "pyrolux"), new Pyrolux());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluvalen"), new Fluvalen());

        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "frigala"), new Frigala());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "cryoluxa"), new Cryoluxa());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "nivalune"), new Nivalune());

        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "voltaris"), new Voltaris());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fulgenza"), new Fulgenza());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "sparkion"), new Sparkion());

        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "tremivox"), new Tremivox());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "oscilbeam"), new Oscilbeam());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "sonistorm"), new Sonistorm());

        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mirazien"), new Mirazien());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "phantastra"), new Phantastra());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "veilmist"), new Veilmist());

        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "arcaleth"), new Arcaleth());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mystaven"), new Mystaven());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "glymora"), new Glymora());

        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "aeltherin"), new Aeltherin());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluvinae"), new Fluvinae());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mistrelune"), new Mistrelune());

        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "nullixis"), new Nullixis());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "voidlance"), new Voidlance());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "tenebrisol"), new Tenebrisol());
        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "disparundra"), new Disparundra());

        SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "enercrux"), new Enercrux());
    }
}
