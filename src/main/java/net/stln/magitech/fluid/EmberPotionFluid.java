package net.stln.magitech.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public class EmberPotionFluid extends VirtualFluid {

    @Override
    public Fluid getFlowing() {
        return FluidInit.EMBER_POTION.get();
    }

    @Override
    public Fluid getSource() {
        return FluidInit.EMBER_POTION.get();
    }

    @Override
    public FluidType getFluidType() {
        return FluidInit.EMBER_POTION_TYPE.get();
    }

}
