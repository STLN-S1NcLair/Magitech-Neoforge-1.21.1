package net.stln.magitech.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.*;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.stln.magitech.item.ItemInit;

import javax.annotation.Nullable;
import java.util.Optional;

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
