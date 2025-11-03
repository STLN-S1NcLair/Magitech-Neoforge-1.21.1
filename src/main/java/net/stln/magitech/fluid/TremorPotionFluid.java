package net.stln.magitech.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public class TremorPotionFluid extends VirtualFluid {

    @Override
    public Fluid getFlowing() {
    return FluidInit.TREMOR_POTION.get();
    }

    @Override
    public Fluid getSource() {
        return FluidInit.TREMOR_POTION.get();
    }

    @Override
    public FluidType getFluidType() {
        return FluidInit.TREMOR_POTION_TYPE.get();
    }

}
