package net.stln.magitech.content.field_effect.influence;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.registry.*;

public class FieldInfluenceInit {
    public static final DeferredFieldInfluenceTypeRegister REGISTER = new DeferredFieldInfluenceTypeRegister(Magitech.MOD_ID);

    public static final DeferredFieldInfluenceType<HeatInfluenceType> HEAT = REGISTER.register("heat", HeatInfluenceType::new);
    public static final DeferredFieldInfluenceType<CoolingInfluenceType> COOLING = REGISTER.register("cooling", CoolingInfluenceType::new);

    public static void registerInfluences(IEventBus bus) {
        Magitech.LOGGER.info("Registering Field Influences for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
