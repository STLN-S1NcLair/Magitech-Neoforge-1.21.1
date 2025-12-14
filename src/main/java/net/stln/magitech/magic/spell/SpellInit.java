package net.stln.magitech.magic.spell;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.spell.ember.*;
import net.stln.magitech.magic.spell.flow.*;
import net.stln.magitech.magic.spell.glace.*;
import net.stln.magitech.magic.spell.hollow.*;
import net.stln.magitech.magic.spell.magic.Arcaleth;
import net.stln.magitech.magic.spell.magic.Envistra;
import net.stln.magitech.magic.spell.magic.Glymora;
import net.stln.magitech.magic.spell.magic.Mystaven;
import net.stln.magitech.magic.spell.mana.Enercrux;
import net.stln.magitech.magic.spell.phantom.*;
import net.stln.magitech.magic.spell.surge.*;
import net.stln.magitech.magic.spell.tremor.*;
import net.stln.magitech.registry.DeferredSpell;
import net.stln.magitech.registry.DeferredSpellRegister;
import org.jetbrains.annotations.NotNull;

public class SpellInit {
    public static final DeferredSpellRegister REGISTER = new DeferredSpellRegister(Magitech.MOD_ID);

    public static final DeferredSpell<Spell> IGNISCA = register("ignisca", new Ignisca());
    public static final DeferredSpell<Spell> PYROLUX = register("pyrolux", new Pyrolux());
    public static final DeferredSpell<Spell> FLUVALEN = register("fluvalen", new Fluvalen());
    public static final DeferredSpell<Spell> BLAZEWEND = register("blazewend", new Blazewend());
    public static final DeferredSpell<Spell> VOLKARIN = register("volkarin", new Volkarin());

    public static final DeferredSpell<Spell> FRIGALA = register("frigala", new Frigala());
    public static final DeferredSpell<Spell> CRYOLUXA = register("cryoluxa", new Cryoluxa());
    public static final DeferredSpell<Spell> NIVALUNE = register("nivalune", new Nivalune());
    public static final DeferredSpell<Spell> GLISTELDA = register("glistelda", new Glistelda());
    public static final DeferredSpell<Spell> FROSBLAST = register("frosblast", new Frosblast());

    public static final DeferredSpell<Spell> VOLTARIS = register("voltaris", new Voltaris());
    public static final DeferredSpell<Spell> FULGENZA = register("fulgenza", new Fulgenza());
    public static final DeferredSpell<Spell> SPARKION = register("sparkion", new Sparkion());
    public static final DeferredSpell<Spell> ARCLUME = register("arclume", new Arclume());
    public static final DeferredSpell<Spell> ELECTROIDE = register("electroide", new Electroide());

    public static final DeferredSpell<Spell> MIRAZIEN = register("mirazien", new Mirazien());
    public static final DeferredSpell<Spell> PHANTASTRA = register("phantastra", new Phantastra());
    public static final DeferredSpell<Spell> VEILMIST = register("veilmist", new Veilmist());
    public static final DeferredSpell<Spell> FADANCEA = register("fadancea", new Fadancea());
    public static final DeferredSpell<Spell> ILLUSFLARE = register("illusflare", new Illusflare());

    public static final DeferredSpell<Spell> TREMIVOX = register("tremivox", new Tremivox());
    public static final DeferredSpell<Spell> OSCILBEAM = register("oscilbeam", new Oscilbeam());
    public static final DeferredSpell<Spell> SONISTORM = register("sonistorm", new Sonistorm());
    public static final DeferredSpell<Spell> QUAVERIS = register("quaveris", new Quaveris());
    public static final DeferredSpell<Spell> SHOCKVANE = register("shockvane", new Shockvane());

    public static final DeferredSpell<Spell> ARCALETH = register("arcaleth", new Arcaleth());
    public static final DeferredSpell<Spell> MYSTAVEN = register("mystaven", new Mystaven());
    public static final DeferredSpell<Spell> GLYMORA = register("glymora", new Glymora());
    public static final DeferredSpell<Spell> ENVISTRA = register("envistra", new Envistra());

    public static final DeferredSpell<Spell> AELTHERIN = register("aeltherin", new Aeltherin());
    public static final DeferredSpell<Spell> FLUVINAE = register("fluvinae", new Fluvinae());
    public static final DeferredSpell<Spell> MISTRELUNE = register("mistrelune", new Mistrelune());
    public static final DeferredSpell<Spell> SYLLAEZE = register("syllaeze", new Syllaeze());
    public static final DeferredSpell<Spell> NYMPHORA = register("nymphora", new Nymphora());

    public static final DeferredSpell<Spell> NULLIXIS = register("nullixis", new Nullixis());
    public static final DeferredSpell<Spell> VOIDLANCE = register("voidlance", new Voidlance());
    public static final DeferredSpell<Spell> TENEBRISOL = register("tenebrisol", new Tenebrisol());
    public static final DeferredSpell<Spell> DISPARUNDRA = register("disparundra", new Disparundra());
    public static final DeferredSpell<Spell> TENEBPORT = register("tenebport", new Tenebport());

    public static final DeferredSpell<Spell> ENERCRUX = register("enercrux", new Enercrux());

    private static @NotNull DeferredSpell<Spell> register(@NotNull String path, @NotNull Spell spell) {
        return REGISTER.register(path, () -> spell);
    }

    public static void registerSpells(IEventBus bus) {
        Magitech.LOGGER.info("Registering Spells for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
