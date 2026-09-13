package net.stln.magitech.feature.tool.trait;

import net.neoforged.bus.api.IEventBus;
import net.stln.magitech.Magitech;
import net.stln.magitech.registry.DeferredTrait;
import net.stln.magitech.registry.DeferredTraitRegister;

public class TraitInit {
    public static final DeferredTraitRegister REGISTER = new DeferredTraitRegister(Magitech.MOD_ID);

    public static final DeferredTrait<Trait> SAMPLE = REGISTER.register("sample", SampleTrait::new);
    public static final DeferredTrait<Trait> ADAPTATION = REGISTER.register("adaptation", AdaptationTrait::new);
    public static final DeferredTrait<Trait> GEOMENDING = REGISTER.register("geomending", GeomendingTrait::new);
    public static final DeferredTrait<Trait> HARDMINE = REGISTER.register("hardmine", HardmineTrait::new);
    public static final DeferredTrait<Trait> CONDUCTANCE = REGISTER.register("conductance", ConductanceTrait::new);
    public static final DeferredTrait<Trait> ELECTROSTATIC_CHARGE = REGISTER.register("electrostatic_charge", ElectrostaticChargeTrait::new);
    public static final DeferredTrait<Trait> FOSSILIZATION = REGISTER.register("fossilization", FossilizationTrait::new);
    public static final DeferredTrait<Trait> FRAGILE = REGISTER.register("fragile", FragileTrait::new);
    public static final DeferredTrait<Trait> TEMPERATURE_DIFFERENCE = REGISTER.register("temperature_difference", TemperatureDifferenceTrait::new);
    public static final DeferredTrait<Trait> GROWTH = REGISTER.register("growth", GrowthTrait::new);
    public static final DeferredTrait<Trait> FROZEN = REGISTER.register("frozen", FrozenTrait::new);
    public static final DeferredTrait<Trait> STURDY = REGISTER.register("sturdy", SturdyTrait::new);
    public static final DeferredTrait<Trait> DURANCE = REGISTER.register("durance", DuranceTrait::new);
    public static final DeferredTrait<Trait> CATALYSIS = REGISTER.register("catalysis", CatalysisTrait::new);
    public static final DeferredTrait<Trait> SHATTER = REGISTER.register("shatter", ShatterTrait::new);
    public static final DeferredTrait<Trait> HEAT_TREATMENT = REGISTER.register("heat_treatment", HeatTreatmentTrait::new);
    public static final DeferredTrait<Trait> SIGNAL_RUSH = REGISTER.register("signal_rush", SignalRushTrait::new);
    public static final DeferredTrait<Trait> INCLUSION = REGISTER.register("inclusion", InclusionTrait::new);
    public static final DeferredTrait<Trait> BIREFRINGENCE = REGISTER.register("birefringence", BirefringenceTrait::new);
    public static final DeferredTrait<Trait> PRECIPITATION = REGISTER.register("precipitation", PrecipitationTrait::new);
    public static final DeferredTrait<Trait> OVERCHARGED = REGISTER.register("overcharged", OverchargedTrait::new);
    public static final DeferredTrait<Trait> ELECTRICAL_BOOST = REGISTER.register("electrical_boost", ElectricalBoostTrait::new);
    public static final DeferredTrait<Trait> COLLAPSE = REGISTER.register("collapse", CollapseTrait::new);
    public static final DeferredTrait<Trait> STICKY = REGISTER.register("sticky", StickyTrait::new);
    public static final DeferredTrait<Trait> PLASTIC = REGISTER.register("plastic", PlasticTrait::new);
    public static final DeferredTrait<Trait> SCORCHED = REGISTER.register("scorched", ScorchedTrait::new);
    public static final DeferredTrait<Trait> INSOMNIA = REGISTER.register("insomnia", InsomniaTrait::new);
    public static final DeferredTrait<Trait> LIGHTWEIGHT = REGISTER.register("lightweight", LightweightTrait::new);
    public static final DeferredTrait<Trait> CONCENTRATION = REGISTER.register("concentration", ConcentrationTrait::new);
    public static final DeferredTrait<Trait> ENDER_DRAW = REGISTER.register("ender_draw", EnderDrawTrait::new);
    public static final DeferredTrait<Trait> SMOOTH = REGISTER.register("smooth", SmoothTrait::new);
    public static final DeferredTrait<Trait> ILLUMINATION = REGISTER.register("illumination", IlluminationTrait::new);
    public static final DeferredTrait<Trait> SPARK = REGISTER.register("spark", SparkTrait::new);
    public static final DeferredTrait<Trait> SEVERING = REGISTER.register("severing", SeveringTrait::new);
    public static final DeferredTrait<Trait> ABNORMALITY = REGISTER.register("abnormality", AbnormalityTrait::new);
    public static final DeferredTrait<Trait> INFUSED = REGISTER.register("infused", InfusedTrait::new);
    public static final DeferredTrait<Trait> TUNING = REGISTER.register("tuning", TuningTrait::new);
    public static final DeferredTrait<Trait> LAVAFORGED = REGISTER.register("lavaforged", LavaforgedTrait::new);
    public static final DeferredTrait<Trait> BRILLIANCE = REGISTER.register("brilliance", BrillianceTrait::new);

    public static void registerTraits(IEventBus bus) {
        Magitech.LOGGER.info("Registering Traits for" + Magitech.MOD_ID);
        REGISTER.register(bus);
    }
}
