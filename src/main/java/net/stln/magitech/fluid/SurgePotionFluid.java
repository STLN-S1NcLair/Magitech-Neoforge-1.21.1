package net.stln.magitech.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public class SurgePotionFluid extends VirtualFluid {

    @Override
    public Fluid getFlowing() {
    return FluidInit.SURGE_POTION.get();
    }

    @Override
    public Fluid getSource() {
        return FluidInit.SURGE_POTION.get();
    }

    @Override
    public FluidType getFluidType() {
        return FluidInit.SURGE_POTION_TYPE.get();
    }

}
