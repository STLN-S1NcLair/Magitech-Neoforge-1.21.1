package net.stln.magitech.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public class SulfuricAcidFluid extends VirtualFluid {

    @Override
    public Fluid getFlowing() {
        return FluidInit.SULFURIC_ACID.get();
    }

    @Override
    public Fluid getSource() {
        return FluidInit.SULFURIC_ACID.get();
    }

    @Override
    public FluidType getFluidType() {
        return FluidInit.SULFURIC_ACID_TYPE.get();
    }

}
