package net.stln.magitech.magic.spell;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
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
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SpellInit {
    public static final DeferredRegister<Spell> REGISTER = DeferredRegister.create(MagitechRegistries.Keys.SPELL, Magitech.MOD_ID);

    public static final Supplier<Spell> IGNISCA = register("ignisca", new Ignisca());
    public static final Supplier<Spell> PYROLUX = register("pyrolux", new Pyrolux());
    public static final Supplier<Spell> FLUVALEN = register("fluvalen", new Fluvalen());
    public static final Supplier<Spell> BLAZEWEND = register("blazewend", new Blazewend());

    public static final Supplier<Spell> FRIGALA = register("frigala", new Frigala());
    public static final Supplier<Spell> CRYOLUXA = register("cryoluxa", new Cryoluxa());
    public static final Supplier<Spell> NIVALUNE = register("nivalune", new Nivalune());
    public static final Supplier<Spell> GLISTELDA = register("glistelda", new Glistelda());

    public static final Supplier<Spell> VOLTARIS = register("voltaris", new Voltaris());
    public static final Supplier<Spell> FULGENZA = register("fulgenza", new Fulgenza());
    public static final Supplier<Spell> SPARKION = register("sparkion", new Sparkion());
    public static final Supplier<Spell> ARCLUME = register("arclume", new Arclume());

    public static final Supplier<Spell> TREMIVOX = register("tremivox", new Tremivox());
    public static final Supplier<Spell> OSCILBEAM = register("oscilbeam", new Oscilbeam());
    public static final Supplier<Spell> SONISTORM = register("sonistorm", new Sonistorm());
    public static final Supplier<Spell> QUAVERIS = register("quaveris", new Quaveris());

    public static final Supplier<Spell> MIRAZIEN = register("mirazien", new Mirazien());
    public static final Supplier<Spell> PHANTASTRA = register("phantastra", new Phantastra());
    public static final Supplier<Spell> VEILMIST = register("veilmist", new Veilmist());
    public static final Supplier<Spell> FADANCEA = register("fadancea", new Fadancea());

    public static final Supplier<Spell> ARCALETH = register("arcaleth", new Arcaleth());
    public static final Supplier<Spell> MYSTAVEN = register("mystaven", new Mystaven());
    public static final Supplier<Spell> GLYMORA = register("glymora", new Glymora());
    public static final Supplier<Spell> ENVISTRA = register("envistra", new Envistra());

    public static final Supplier<Spell> AELTHERIN = register("aeltherin", new Aeltherin());
    public static final Supplier<Spell> FLUVINAE = register("fluvinae", new Fluvinae());
    public static final Supplier<Spell> MISTRELUNE = register("mistrelune", new Mistrelune());
    public static final Supplier<Spell> SYLLAEZE = register("syllaeze", new Syllaeze());
    public static final Supplier<Spell> NYMPHORA = register("nymphora", new Nymphora());

    public static final Supplier<Spell> NULLIXIS = register("nullixis", new Nullixis());
    public static final Supplier<Spell> VOIDLANCE = register("voidlance", new Voidlance());
    public static final Supplier<Spell> TENEBRISOL = register("tenebrisol", new Tenebrisol());
    public static final Supplier<Spell> DISPARUNDRA = register("disparundra", new Disparundra());
    public static final Supplier<Spell> TENEBPORT = register("tenebport", new Tenebport());

    public static final Supplier<Spell> ENERCRUX = register("enercrux", new Enercrux());

    private static @NotNull Supplier<Spell> register(@NotNull String path, @NotNull Spell spell) {
        return REGISTER.register(path, () -> spell);
    }
    
    public static void registerSpells(IEventBus bus) {
        Magitech.LOGGER.info("Registering Spells for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
