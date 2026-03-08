package net.stln.magitech.feature.magic.spell;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.magic.spell.spell.ember.*;
import net.stln.magitech.feature.magic.spell.spell.flow.*;
import net.stln.magitech.feature.magic.spell.spell.glace.*;
import net.stln.magitech.feature.magic.spell.spell.hollow.*;
import net.stln.magitech.feature.magic.spell.spell.magic.*;
import net.stln.magitech.feature.magic.spell.spell.mana.Enercrux;
import net.stln.magitech.feature.magic.spell.spell.phantom.*;
import net.stln.magitech.feature.magic.spell.spell.surge.*;
import net.stln.magitech.feature.magic.spell.spell.tremor.*;
import net.stln.magitech.registry.DeferredSpell;
import net.stln.magitech.registry.DeferredSpellRegister;
import org.jetbrains.annotations.NotNull;

public class SpellInit {
    public static final DeferredSpellRegister REGISTER = new DeferredSpellRegister(Magitech.MOD_ID);

    public static final DeferredSpell<ISpell> IGNISCA = register("ignisca", new Ignisca());
    public static final DeferredSpell<ISpell> PYROLUX = register("pyrolux", new Pyrolux());
    public static final DeferredSpell<ISpell> FLUVALEN = register("fluvalen", new Fluvalen());
    public static final DeferredSpell<ISpell> BLAZEWEND = register("blazewend", new Blazewend());
    public static final DeferredSpell<ISpell> VOLKARIN = register("volkarin", new Volkarin());
    public static final DeferredSpell<ISpell> ARDOVITAE = register("ardovitae", new Ardovitae());

    public static final DeferredSpell<ISpell> FRIGALA = register("frigala", new Frigala());
    public static final DeferredSpell<ISpell> CRYOLUXA = register("cryoluxa", new Cryoluxa());
    public static final DeferredSpell<ISpell> NIVALUNE = register("nivalune", new Nivalune());
    public static final DeferredSpell<ISpell> GLISTELDA = register("glistelda", new Glistelda());
    public static final DeferredSpell<ISpell> FROSBLAST = register("frosblast", new Frosblast());

    public static final DeferredSpell<ISpell> VOLTARIS = register("voltaris", new Voltaris());
    public static final DeferredSpell<ISpell> FULGENZA = register("fulgenza", new Fulgenza());
    public static final DeferredSpell<ISpell> SPARKION = register("sparkion", new Sparkion());
    public static final DeferredSpell<ISpell> ARCLUME = register("arclume", new Arclume());
    public static final DeferredSpell<ISpell> ELECTROIDE = register("electroide", new Electroide());

    public static final DeferredSpell<ISpell> MIRAZIEN = register("mirazien", new Mirazien());
    public static final DeferredSpell<ISpell> PHANTASTRA = register("phantastra", new Phantastra());
    public static final DeferredSpell<ISpell> VEILMIST = register("veilmist", new Veilmist());
    public static final DeferredSpell<ISpell> FADANCEA = register("fadancea", new Fadancea());
    public static final DeferredSpell<ISpell> ILLUSFLARE = register("illusflare", new Illusflare());
    public static final DeferredSpell<ISpell> LUXGRAIL = register("luxgrail", new Luxgrail());

    public static final DeferredSpell<ISpell> TREMIVOX = register("tremivox", new Tremivox());
    public static final DeferredSpell<ISpell> OSCILBEAM = register("oscilbeam", new Oscilbeam());
    public static final DeferredSpell<ISpell> SONISTORM = register("sonistorm", new Sonistorm());
    public static final DeferredSpell<ISpell> QUAVERIS = register("quaveris", new Quaveris());
    public static final DeferredSpell<ISpell> SHOCKVANE = register("shockvane", new Shockvane());

    public static final DeferredSpell<ISpell> ARCALETH = register("arcaleth", new Arcaleth());
    public static final DeferredSpell<ISpell> MYSTAVEN = register("mystaven", new Mystaven());
    public static final DeferredSpell<ISpell> GLYMORA = register("glymora", new Glymora());
    public static final DeferredSpell<ISpell> ENVISTRA = register("envistra", new Envistra());
    public static final DeferredSpell<ISpell> HEXFLARE = register("hexflare", new Hexflare());
    public static final DeferredSpell<ISpell> MYSTPHEL = register("mystphel", new Mystphel());

    public static final DeferredSpell<ISpell> AELTHERIN = register("aeltherin", new Aeltherin());
    public static final DeferredSpell<ISpell> FLUVINAE = register("fluvinae", new Fluvinae());
    public static final DeferredSpell<ISpell> MISTRELUNE = register("mistrelune", new Mistrelune());
    public static final DeferredSpell<ISpell> SYLLAEZE = register("syllaeze", new Syllaeze());
    public static final DeferredSpell<ISpell> HYDRELUX = register("hydrelux", new Hydrelux());
    public static final DeferredSpell<ISpell> NYMPHORA = register("nymphora", new Nymphora());
    public static final DeferredSpell<ISpell> HYDRAERUN = register("hydraerun", new Hydraerun());

    public static final DeferredSpell<ISpell> NULLIXIS = register("nullixis", new Nullixis());
    public static final DeferredSpell<ISpell> VOIDLANCE = register("voidlance", new Voidlance());
    public static final DeferredSpell<ISpell> TENEBRISOL = register("tenebrisol", new Tenebrisol());
    public static final DeferredSpell<ISpell> DISPARUNDRA = register("disparundra", new Disparundra());
    public static final DeferredSpell<ISpell> NIHILFLARE = register("nihilflare", new Nihilflare());
    public static final DeferredSpell<ISpell> TENEBPORT = register("tenebport", new Tenebport());

    public static final DeferredSpell<ISpell> ENERCRUX = register("enercrux", new Enercrux());

    private static @NotNull DeferredSpell<ISpell> register(@NotNull String path, @NotNull ISpell spell) {
        return REGISTER.register(path, () -> spell);
    }

    public static void registerSpells(IEventBus bus) {
        Magitech.LOGGER.info("Registering Spells for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
