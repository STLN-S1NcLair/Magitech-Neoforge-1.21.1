package net.stln.magitech.magic.spell;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.spell.ember.Blazewend;
import net.stln.magitech.magic.spell.ember.Fluvalen;
import net.stln.magitech.magic.spell.ember.Ignisca;
import net.stln.magitech.magic.spell.ember.Pyrolux;
import net.stln.magitech.magic.spell.flow.*;
import net.stln.magitech.magic.spell.glace.Cryoluxa;
import net.stln.magitech.magic.spell.glace.Frigala;
import net.stln.magitech.magic.spell.glace.Glistelda;
import net.stln.magitech.magic.spell.glace.Nivalune;
import net.stln.magitech.magic.spell.hollow.*;
import net.stln.magitech.magic.spell.magic.Arcaleth;
import net.stln.magitech.magic.spell.magic.Envistra;
import net.stln.magitech.magic.spell.magic.Glymora;
import net.stln.magitech.magic.spell.magic.Mystaven;
import net.stln.magitech.magic.spell.mana.Enercrux;
import net.stln.magitech.magic.spell.phantom.Fadancea;
import net.stln.magitech.magic.spell.phantom.Mirazien;
import net.stln.magitech.magic.spell.phantom.Phantastra;
import net.stln.magitech.magic.spell.phantom.Veilmist;
import net.stln.magitech.magic.spell.surge.Arclume;
import net.stln.magitech.magic.spell.surge.Fulgenza;
import net.stln.magitech.magic.spell.surge.Sparkion;
import net.stln.magitech.magic.spell.surge.Voltaris;
import net.stln.magitech.magic.spell.tremor.Oscilbeam;
import net.stln.magitech.magic.spell.tremor.Quaveris;
import net.stln.magitech.magic.spell.tremor.Sonistorm;
import net.stln.magitech.magic.spell.tremor.Tremivox;

public class SpellInit {
    public static final Spell IGNISCA = SpellRegister.registerSpell(Magitech.id("ignisca"), new Ignisca());
    public static final Spell PYROLUX = SpellRegister.registerSpell(Magitech.id("pyrolux"), new Pyrolux());
    public static final Spell FLUVALEN = SpellRegister.registerSpell(Magitech.id("fluvalen"), new Fluvalen());
    public static final Spell BLAZEWEND = SpellRegister.registerSpell(Magitech.id("blazewend"), new Blazewend());

    public static final Spell FRIGALA = SpellRegister.registerSpell(Magitech.id("frigala"), new Frigala());
    public static final Spell CRYOLUXA = SpellRegister.registerSpell(Magitech.id("cryoluxa"), new Cryoluxa());
    public static final Spell NIVALUNE = SpellRegister.registerSpell(Magitech.id("nivalune"), new Nivalune());
    public static final Spell GLISTELDA = SpellRegister.registerSpell(Magitech.id("glistelda"), new Glistelda());

    public static final Spell VOLTARIS = SpellRegister.registerSpell(Magitech.id("voltaris"), new Voltaris());
    public static final Spell FULGENZA = SpellRegister.registerSpell(Magitech.id("fulgenza"), new Fulgenza());
    public static final Spell SPARKION = SpellRegister.registerSpell(Magitech.id("sparkion"), new Sparkion());
    public static final Spell ARCLUME = SpellRegister.registerSpell(Magitech.id("arclume"), new Arclume());

    public static final Spell TREMIVOX = SpellRegister.registerSpell(Magitech.id("tremivox"), new Tremivox());
    public static final Spell OSCILBEAM = SpellRegister.registerSpell(Magitech.id("oscilbeam"), new Oscilbeam());
    public static final Spell SONISTORM = SpellRegister.registerSpell(Magitech.id("sonistorm"), new Sonistorm());
    public static final Spell QUAVERIS = SpellRegister.registerSpell(Magitech.id("quaveris"), new Quaveris());

    public static final Spell MIRAZIEN = SpellRegister.registerSpell(Magitech.id("mirazien"), new Mirazien());
    public static final Spell PHANTASTRA = SpellRegister.registerSpell(Magitech.id("phantastra"), new Phantastra());
    public static final Spell VEILMIST = SpellRegister.registerSpell(Magitech.id("veilmist"), new Veilmist());
    public static final Spell FADANCEA = SpellRegister.registerSpell(Magitech.id("fadancea"), new Fadancea());

    public static final Spell ARCALETH = SpellRegister.registerSpell(Magitech.id("arcaleth"), new Arcaleth());
    public static final Spell MYSTAVEN = SpellRegister.registerSpell(Magitech.id("mystaven"), new Mystaven());
    public static final Spell GLYMORA = SpellRegister.registerSpell(Magitech.id("glymora"), new Glymora());
    public static final Spell ENVISTRA = SpellRegister.registerSpell(Magitech.id("envistra"), new Envistra());

    public static final Spell AELTHERIN = SpellRegister.registerSpell(Magitech.id("aeltherin"), new Aeltherin());
    public static final Spell FLUVINAE = SpellRegister.registerSpell(Magitech.id("fluvinae"), new Fluvinae());
    public static final Spell MISTRELUNE = SpellRegister.registerSpell(Magitech.id("mistrelune"), new Mistrelune());
    public static final Spell SYLLAEZE = SpellRegister.registerSpell(Magitech.id("syllaeze"), new Syllaeze());
    public static final Spell NYMPHORA = SpellRegister.registerSpell(Magitech.id("nymphora"), new Nymphora());

    public static final Spell NULLIXIS = SpellRegister.registerSpell(Magitech.id("nullixis"), new Nullixis());
    public static final Spell VOIDLANCE = SpellRegister.registerSpell(Magitech.id("voidlance"), new Voidlance());
    public static final Spell TENEBRISOL = SpellRegister.registerSpell(Magitech.id("tenebrisol"), new Tenebrisol());
    public static final Spell DISPARUNDRA = SpellRegister.registerSpell(Magitech.id("disparundra"), new Disparundra());
    public static final Spell TENEBPORT = SpellRegister.registerSpell(Magitech.id("tenebport"), new Tenebport());

    public static final Spell ENERCRUX = SpellRegister.registerSpell(Magitech.id("enercrux"), new Enercrux());

    public static void registerSpells() {
        Magitech.LOGGER.info("Registering Spells for" + Magitech.MOD_ID);
    }
}
