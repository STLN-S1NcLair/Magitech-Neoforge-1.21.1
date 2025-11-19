package net.stln.magitech.fluid;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public class MagicPotionFluid extends VirtualFluid {

    @Override
    public Fluid getFlowing() {
        return FluidInit.MAGIC_POTION.get();
    }

    @Override
    public Fluid getSource() {
        return FluidInit.MAGIC_POTION.get();
    }

    @Override
    public FluidType getFluidType() {
        return FluidInit.MAGIC_POTION_TYPE.get();
    }

}
