package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;

import java.awt.*;

public class RedstoneCrystalClusterBlock extends CrystalClusterBlock {

    public RedstoneCrystalClusterBlock(IntProvider xpRange, Properties properties) {
        super(xpRange, properties);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 15;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(FACING) == direction ? 15 : 0;
    }

    private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos.relative(state.getValue(FACING).getOpposite()), this);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.updateNeighbours(state, level, pos);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        PointVFX.burstColored(level, pos.getCenter(), new Color(0xFF3000), new Color(0x800000), SquareParticles::squareParticleColored, 2, 0.5F);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            this.updateNeighbours(state, level, pos);

            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }
}
