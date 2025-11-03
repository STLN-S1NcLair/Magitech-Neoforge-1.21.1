package net.stln.magitech.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public class FlowPotionFluid extends VirtualFluid {

    @Override
    public Fluid getFlowing() {
    return FluidInit.FLOW_POTION.get();
    }

    @Override
    public Fluid getSource() {
        return FluidInit.FLOW_POTION.get();
    }

    @Override
    public FluidType getFluidType() {
        return FluidInit.FLOW_POTION_TYPE.get();
    }

}
