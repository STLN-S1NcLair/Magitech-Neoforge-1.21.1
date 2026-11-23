package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.FluoritePillarBlock;

public class FluoritePillarBlockEntity extends BlockEntity {
    private int tickCounter;

    public FluoritePillarBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.FLUORITE_PILLAR_ENTITY.get(), pos, state);
    }

    public void clientTick(Level level, BlockPos pos, BlockState state) {
        FluoritePillarBlock.spawnOrbitingParticles(level, pos, state.getValue(RotatedPillarBlock.AXIS), ++tickCounter);
    }
}
