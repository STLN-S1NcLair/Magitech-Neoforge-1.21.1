package net.stln.magitech.sound;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;

import java.util.function.Supplier;

public class SoundInit {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Magitech.MOD_ID);

    public static final Supplier<SoundEvent> PHANTOM_BUFF = registerSoundEvent("phantom_buff");
    public static final Supplier<SoundEvent> GEOMENDING_BREAK = registerSoundEvent("geomending_break");
    public static final Supplier<SoundEvent> PHANTOM_SLAYER_DASH = registerSoundEvent("phantom_slayer_dash");
    public static final Supplier<SoundEvent> FROST_BREAK = registerSoundEvent("frost_break");
    public static final Supplier<SoundEvent> ZAP = registerSoundEvent("zap");
    public static final Supplier<SoundEvent> FLAME = registerSoundEvent("flame");
    public static final Supplier<SoundEvent> SONICBOOM = registerSoundEvent("sonicboom");
    public static final Supplier<SoundEvent> MYSTICAL = registerSoundEvent("mystical");
    public static final Supplier<SoundEvent> GLACE_LAUNCH = registerSoundEvent("glace_launch");
    public static final Supplier<SoundEvent> SPARK = registerSoundEvent("spark");
    public static final Supplier<SoundEvent> BLOW = registerSoundEvent("blow");
    public static final Supplier<SoundEvent> ARCALETH = registerSoundEvent("arcaleth");
    public static final Supplier<SoundEvent> FIREBALL = registerSoundEvent("fireball");
    public static final Supplier<SoundEvent> TREMIVOX = registerSoundEvent("tremivox");
    public static final Supplier<SoundEvent> AELTHERIN = registerSoundEvent("aeltherin");
    public static final Supplier<SoundEvent> NULLIXIS = registerSoundEvent("nullixis");
    public static final Supplier<SoundEvent> NIVALUNE = registerSoundEvent("nivalune");
    public static final Supplier<SoundEvent> FULGENZA = registerSoundEvent("fulgenza");
    public static final Supplier<SoundEvent> FULGENZA_CHARGE = registerSoundEvent("fulgenza_charge");
    public static final Supplier<SoundEvent> VOIDLANCE = registerSoundEvent("voidlance");
    public static final Supplier<SoundEvent> MYSTAVEN = registerSoundEvent("mystaven");
    public static final Supplier<SoundEvent> PHANTASTRA = registerSoundEvent("phantastra");
    public static final Supplier<SoundEvent> PYROLUX = registerSoundEvent("pyrolux");
    public static final Supplier<SoundEvent> FLUVINAE = registerSoundEvent("fluvinae");
    public static final Supplier<SoundEvent> VEILMIST = registerSoundEvent("veilmist");
    public static final Supplier<SoundEvent> SONISTORM = registerSoundEvent("sonistorm");
    public static final Supplier<SoundEvent> GLYMORA = registerSoundEvent("glymora");
    public static final Supplier<SoundEvent> TENEBRISOL = registerSoundEvent("tenebrisol");
    public static final Supplier<SoundEvent> ECHOLOCATION = registerSoundEvent("echolocation");
    public static final Supplier<SoundEvent> ENERCRUX = registerSoundEvent("enercrux");
    public static final Supplier<SoundEvent> SYLLAEZE = registerSoundEvent("syllaeze");
    public static final Supplier<SoundEvent> ARCLUME = registerSoundEvent("arclume");

    public static final Supplier<SoundEvent> AETHER_LIFTER_JUMP = registerSoundEvent("aether_lifter_jump");
    public static final Supplier<SoundEvent> FLAMGLIDE_STRIDER_JUMP = registerSoundEvent("flamglide_strider_jump");

    public static final Supplier<SoundEvent> ATHANOR_PILLAR_INFUSION = registerSoundEvent("athanor_pillar_infusion");
    public static final Supplier<SoundEvent> ATHANOR_PILLAR_ZAP = registerSoundEvent("athanor_pillar_zap");

    public static final Supplier<SoundEvent> CRYSTAL_BREAK = registerSoundEvent("crystal_break");
    public static final Supplier<SoundEvent> CRYSTAL_PLACE = registerSoundEvent("crystal_place");
    public static final Supplier<SoundEvent> CRYSTAL_HIT = registerSoundEvent("crystal_hit");
    public static final Supplier<SoundEvent> CRYSTAL_STEP = registerSoundEvent("crystal_step");
    public static final Supplier<SoundEvent> CRYSTAL_FALL = registerSoundEvent("crystal_fall");

    public static final Supplier<SoundEvent> ALCHECRYSITE_BREAK = registerSoundEvent("alchecrysite_break");
    public static final Supplier<SoundEvent> ALCHECRYSITE_PLACE = registerSoundEvent("alchecrysite_place");
    public static final Supplier<SoundEvent> ALCHECRYSITE_HIT = registerSoundEvent("alchecrysite_hit");
    public static final Supplier<SoundEvent> ALCHECRYSITE_STEP = registerSoundEvent("alchecrysite_step");
    public static final Supplier<SoundEvent> ALCHECRYSITE_FALL = registerSoundEvent("alchecrysite_fall");


    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSoundEvents(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Sound for " + Magitech.MOD_ID);
        SOUND_EVENTS.register(eventBus);
    }
}
