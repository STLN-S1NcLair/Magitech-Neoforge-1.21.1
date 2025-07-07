package net.stln.magitech.magic.spell;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.spell.ember.Fluvalen;
import net.stln.magitech.magic.spell.ember.Ignisca;
import net.stln.magitech.magic.spell.ember.Pyrolux;
import net.stln.magitech.magic.spell.flow.Aeltherin;
import net.stln.magitech.magic.spell.flow.Fluvinae;
import net.stln.magitech.magic.spell.flow.Mistrelune;
import net.stln.magitech.magic.spell.flow.Syllaeze;
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
import net.stln.magitech.magic.spell.surge.Arclume;
import net.stln.magitech.magic.spell.surge.Fulgenza;
import net.stln.magitech.magic.spell.surge.Sparkion;
import net.stln.magitech.magic.spell.surge.Voltaris;
import net.stln.magitech.magic.spell.tremor.Oscilbeam;
import net.stln.magitech.magic.spell.tremor.Sonistorm;
import net.stln.magitech.magic.spell.tremor.Tremivox;

public class SpellInit {
    public static final Spell IGNISCA = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ignisca"), new Ignisca());
    public static final Spell PYROLUX = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "pyrolux"), new Pyrolux());
    public static final Spell FLUVALEN = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluvalen"), new Fluvalen());

    public static final Spell FRIGALA = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "frigala"), new Frigala());
    public static final Spell CRYOLUXA = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "cryoluxa"), new Cryoluxa());
    public static final Spell NIVALUNE = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "nivalune"), new Nivalune());

    public static final Spell VOLTARIS = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "voltaris"), new Voltaris());
    public static final Spell FULGENZA = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fulgenza"), new Fulgenza());
    public static final Spell SPARKION = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "sparkion"), new Sparkion());
    public static final Spell ARCLUME = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "arclume"), new Arclume());

    public static final Spell TREMIVOX = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "tremivox"), new Tremivox());
    public static final Spell OSCILBEAM = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "oscilbeam"), new Oscilbeam());
    public static final Spell SONISTORM = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "sonistorm"), new Sonistorm());

    public static final Spell MIRAZIEN = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mirazien"), new Mirazien());
    public static final Spell PHANTASTRA = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "phantastra"), new Phantastra());
    public static final Spell VEILMIST = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "veilmist"), new Veilmist());

    public static final Spell ARCALETH = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "arcaleth"), new Arcaleth());
    public static final Spell MYSTAVEN = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mystaven"), new Mystaven());
    public static final Spell GLYMORA = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "glymora"), new Glymora());

    public static final Spell AELTHERIN = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "aeltherin"), new Aeltherin());
    public static final Spell FLUVINAE = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "fluvinae"), new Fluvinae());
    public static final Spell MISTRELUNE = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mistrelune"), new Mistrelune());
    public static final Spell SYLLAEZE = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "syllaeze"), new Syllaeze());

    public static final Spell NULLIXIS = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "nullixis"), new Nullixis());
    public static final Spell VOIDLANCE = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "voidlance"), new Voidlance());
    public static final Spell TENEBRISOL = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "tenebrisol"), new Tenebrisol());
    public static final Spell DISPARUNDRA = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "disparundra"), new Disparundra());

    public static final Spell ENERCRUX = SpellRegister.registerSpell(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "enercrux"), new Enercrux());

    public static void registerSpells() {
    }
}
