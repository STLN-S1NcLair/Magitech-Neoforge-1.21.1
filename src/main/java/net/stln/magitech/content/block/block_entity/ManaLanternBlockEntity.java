package net.stln.magitech.content.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.ManaLanternBlock;

public class ManaLanternBlockEntity extends BlockEntity {
    public ManaLanternBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.MANA_LANTERN_ENTITY.get(), pos, state);
    }

    public void clientTick(Level level, BlockPos pos) {
        double hangingOffset = getBlockState().getValue(ManaLanternBlock.HANGING) ? 0.125 : 0.0;
        ManaLanternBlock.spawnParticles(level, pos, hangingOffset, level.getGameTime());
    }
}
