package net.stln.magitech.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public class HealingPotionFluid extends VirtualFluid {

    @Override
    public Fluid getFlowing() {
    return FluidInit.HEALING_POTION.get();
    }

    @Override
    public Fluid getSource() {
        return FluidInit.HEALING_POTION.get();
    }

    @Override
    public FluidType getFluidType() {
        return FluidInit.HEALING_POTION_TYPE.get();
    }

}
