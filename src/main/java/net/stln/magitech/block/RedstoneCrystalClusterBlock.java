package net.stln.magitech.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import org.joml.Vector3f;

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
        if ((long) level.random.nextInt(200) <= level.getGameTime() % 200L) {
            Vec3 vec3 = Vec3.atCenterOf(pos);
            double d0 = vec3.x + Mth.nextDouble(level.random, -1.0, 1.0) * (0.4);
            double d1 = vec3.y + Mth.nextDouble(level.random, -1.0, 1.0) * (0.4);
            double d2 = vec3.z + Mth.nextDouble(level.random, -1.0, 1.0) * (0.4);
            double d3 = Mth.nextDouble(level.random, -0.2, 0.2);
            double d4 = Mth.nextDouble(level.random, -0.2, 0.2);
            double d5 = Mth.nextDouble(level.random, -0.2, 0.2);
            level.addParticle(new UnstableSquareParticleEffect(new Vector3f(1.0F, 0.0F, 0.0F), new Vector3f(1.0F, 0.0F, 0.0F), 1.0F, 5, 0, 15, 1.0F), d0, d1, d2, d3, d4, d5);
        }
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
