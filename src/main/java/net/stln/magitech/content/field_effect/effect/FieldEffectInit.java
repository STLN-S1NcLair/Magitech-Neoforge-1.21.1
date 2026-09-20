package net.stln.magitech.content.field_effect.effect;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.registry.DeferredFieldEffectType;
import net.stln.magitech.registry.DeferredFieldEffectTypeRegister;

public class FieldEffectInit {
    public static final DeferredFieldEffectTypeRegister REGISTER = new DeferredFieldEffectTypeRegister(Magitech.MOD_ID);

    public static final DeferredFieldEffectType<FieldEffectType> HEATED = REGISTER.register("heated", HeatedEffectType::new);
    public static final DeferredFieldEffectType<FieldEffectType> SCORCHING = REGISTER.register("scorching", ScorchingEffectType::new);
    public static final DeferredFieldEffectType<FieldEffectType> COLD = REGISTER.register("cold", ColdEffectType::new);
    public static final DeferredFieldEffectType<FieldEffectType> FREEZING = REGISTER.register("freezing", FreezingEffectType::new);
    public static final DeferredFieldEffectType<FieldEffectType> THERMAL_SHOCK = REGISTER.register("thermal_shock", ThermalShockEffectType::new);

    public static void registerEffects(IEventBus bus) {
        Magitech.LOGGER.info("Registering Field Effects for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
