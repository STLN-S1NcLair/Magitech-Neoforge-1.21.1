package net.stln.magitech.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.stln.magitech.Magitech;

public class FluidInit {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Magitech.MOD_ID);

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Magitech.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> SULFURIC_ACID = registerFluid("sulfuric_acid", new SourceF(
            FluidType.Properties.create()
    ));

    public static final DeferredHolder<FluidType, FluidType> SULFURIC_ACID_TYPE = registerFluidType("sulfuric_acid", new FluidType(
            FluidType.Properties.create()
    ));

    private static <S extends Fluid> DeferredHolder<Fluid, S> registerFluid(String name, S type) {
        return FLUIDS.register(name, () -> type);
    }

    private static <S extends FluidType> DeferredHolder<FluidType, S> registerFluidType(String name, S type) {
        return FLUID_TYPES.register(name, () -> type);
    }

    public static void registerFluids(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Fluids for" + Magitech.MOD_ID);
        FLUID_TYPES.register(eventBus);
    }
}
