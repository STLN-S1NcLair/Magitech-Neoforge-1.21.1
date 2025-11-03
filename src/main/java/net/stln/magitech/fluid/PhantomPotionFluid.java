package net.stln.magitech.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public class PhantomPotionFluid extends VirtualFluid {

    @Override
    public Fluid getFlowing() {
    return FluidInit.PHANTOM_POTION.get();
    }

    @Override
    public Fluid getSource() {
        return FluidInit.PHANTOM_POTION.get();
    }

    @Override
    public FluidType getFluidType() {
        return FluidInit.PHANTOM_POTION_TYPE.get();
    }

}
