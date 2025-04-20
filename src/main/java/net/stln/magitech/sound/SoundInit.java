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


    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSoundEvents(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Sound for " + Magitech.MOD_ID);
        SOUND_EVENTS.register(eventBus);
    }
}
