package net.stln.magitech.worldgen.terrain;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.stln.magitech.block.BlockInit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class ScorchedLavaLakeFeature extends Feature<NoneFeatureConfiguration> {

    public ScorchedLavaLakeFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    private static boolean isExposed(WorldGenLevel level, Set<BlockPos> positions, BlockPos pos, BlockPos.MutableBlockPos mutablePos) {
        return isExposedDirection(level, pos, mutablePos, Direction.NORTH)
                || isExposedDirection(level, pos, mutablePos, Direction.EAST)
                || isExposedDirection(level, pos, mutablePos, Direction.SOUTH)
                || isExposedDirection(level, pos, mutablePos, Direction.WEST)
                || isExposedDirection(level, pos, mutablePos, Direction.DOWN);
    }

    private static boolean isExposedDirection(WorldGenLevel level, BlockPos pos, BlockPos.MutableBlockPos mutablePos, Direction direction) {
        mutablePos.setWithOffset(pos, direction);
        return !level.getBlockState(mutablePos).isFaceSturdy(level, mutablePos, direction.getOpposite());
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        NoneFeatureConfiguration none = context.config();
        RandomSource randomsource = context.random();
        BlockPos blockpos = context.origin();
        Predicate<BlockState> predicate = p_204782_ -> p_204782_.is(BlockInit.SCORCHED_GRASS_SOIL.get()) || p_204782_.is(BlockInit.SCORCHED_SOIL.get()) || p_204782_.is(Blocks.STONE);
        int i = randomsource.nextInt(4, 7) + 1;
        int j = randomsource.nextInt(4, 7) + 1;
        Set<BlockPos> set = this.placeGroundPatch(worldgenlevel, none, randomsource, blockpos, predicate, i, j);
        return !set.isEmpty();
    }

    protected Set<BlockPos> placeGroundPatch(
            WorldGenLevel level,
            NoneFeatureConfiguration config,
            RandomSource random,
            BlockPos pos,
            Predicate<BlockState> state,
            int xRadius,
            int zRadius
    ) {
        Set<BlockPos> set = placeGroundPatch1(level, config, random, pos, state, xRadius, zRadius);
        Set<BlockPos> set1 = new HashSet<>();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (BlockPos blockpos : set) {
            if (!isExposed(level, set, blockpos, blockpos$mutableblockpos)) {
                set1.add(blockpos);
            }
        }

        for (BlockPos blockpos1 : set1) {
            level.setBlock(blockpos1, Blocks.LAVA.defaultBlockState(), 2);

            Predicate<BlockState> predicate = p_204782_ -> p_204782_.is(BlockInit.SCORCHED_GRASS_SOIL.get()) || p_204782_.is(BlockInit.SCORCHED_SOIL.get()) || p_204782_.is(Blocks.STONE);
            List<Direction> directions = new java.util.ArrayList<>(Arrays.stream(Direction.values().clone()).toList());
            directions.remove(Direction.UP);
            for (Direction direction : directions) {
                BlockPos pos1 = blockpos1.relative(direction);
                BlockState blockState = level.getBlockState(pos1);
                if (predicate.test(blockState)) {
                    level.setBlock(pos1, Blocks.SMOOTH_BASALT.defaultBlockState(), 2);
                }
            }
        }

        return set1;
    }

    protected Set<BlockPos> placeGroundPatch1(
            WorldGenLevel level,
            NoneFeatureConfiguration config,
            RandomSource random,
            BlockPos pos,
            Predicate<BlockState> state,
            int xRadius,
            int zRadius
    ) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = blockpos$mutableblockpos.mutable();
        Direction direction = Direction.DOWN;
        Direction direction1 = direction.getOpposite();
        Set<BlockPos> set = new HashSet<>();

        for (int i = -xRadius; i <= xRadius; i++) {
            boolean flag = i == -xRadius || i == xRadius;

            for (int j = -zRadius; j <= zRadius; j++) {
                boolean flag1 = j == -zRadius || j == zRadius;
                boolean flag2 = flag || flag1;
                boolean flag3 = flag && flag1;
                boolean flag4 = flag2 && !flag3;
                double rot = Math.toRadians(random.nextInt(360));
                float f1 = (float) Mth.abs((float) (Math.cos(rot) * i - Math.sin(rot) * j)) - 0.25F;
                float f2 = (float) Mth.abs((float) (Math.sin(rot) * i + Math.cos(rot) * j)) - 0.25F;

                // --- 楕円形の判定 + 中心のずらし ---
                float dist = (f1 * f1) / (xRadius * xRadius) + (f2 * f2) / (zRadius * zRadius);

                if (dist <= 0.9 && !flag3 && (!flag4 || 0.7F != 0.0F && !(random.nextFloat() > 0.7F))) {
                    blockpos$mutableblockpos.setWithOffset(pos, i, 0, j);

                    for (int k = 0;
                         level.isStateAtPosition(blockpos$mutableblockpos, BlockBehaviour.BlockStateBase::isAir) && k < 5;
                         k++
                    ) {
                        blockpos$mutableblockpos.move(direction);
                    }

                    for (int i1 = 0;
                         level.isStateAtPosition(blockpos$mutableblockpos, p_284926_ -> !p_284926_.isAir()) && i1 < 5;
                         i1++
                    ) {
                        blockpos$mutableblockpos.move(direction1);
                    }

                    blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.DOWN);
                    BlockState blockstate = level.getBlockState(blockpos$mutableblockpos1);
                    if (level.isEmptyBlock(blockpos$mutableblockpos)
                            && blockstate.isFaceSturdy(level, blockpos$mutableblockpos1, Direction.DOWN.getOpposite())) {
                        int l = 3
                                + (0.8F > 0.0F && random.nextFloat() < 0.8F ? 1 : 0);
                        BlockPos blockpos = blockpos$mutableblockpos1.immutable();
                        boolean flag5 = this.placeGround(level, config, state, random, blockpos$mutableblockpos1, l);
                        if (flag5) {
                            set.add(blockpos);
                        }
                    }
                }
            }
        }

        return set;
    }

    protected boolean placeGround(
            WorldGenLevel level,
            NoneFeatureConfiguration config,
            Predicate<BlockState> replaceableblocks,
            RandomSource random,
            BlockPos.MutableBlockPos mutablePos,
            int maxDistance
    ) {
        for (int i = 0; i < maxDistance; i++) {
            BlockState blockstate = Blocks.SMOOTH_BASALT.defaultBlockState();
            BlockState blockstate1 = level.getBlockState(mutablePos);
            if (!blockstate.is(blockstate1.getBlock())) {
                if (!replaceableblocks.test(blockstate1)) {
                    return i != 0;
                }

                level.setBlock(mutablePos, blockstate, 2);
                mutablePos.move(Direction.DOWN);
            }
        }

        return true;
    }
}
