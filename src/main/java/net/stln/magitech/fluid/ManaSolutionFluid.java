package net.stln.magitech.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public class ManaSolutionFluid extends VirtualFluid {

    @Override
    public Fluid getFlowing() {
        return FluidInit.MANA_SOLUTION.get();
    }

    @Override
    public Fluid getSource() {
        return FluidInit.MANA_SOLUTION.get();
    }

    @Override
    public FluidType getFluidType() {
        return FluidInit.MANA_SOLUTION_TYPE.get();
    }

}
