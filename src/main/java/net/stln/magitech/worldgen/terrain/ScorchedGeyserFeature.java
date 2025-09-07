package net.stln.magitech.worldgen.terrain;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.CrystalClusterBlock;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static net.minecraft.core.Direction.Plane.HORIZONTAL;

public class ScorchedGeyserFeature extends Feature<NoneFeatureConfiguration> {
    public ScorchedGeyserFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        BlockPos blockpos = ctx.origin();
        RandomSource random = ctx.random();
        WorldGenLevel level = ctx.level();
        int height = random.nextInt(30);

        addHill(level, blockpos, random, height);
        int count = 2;
        while (height / count > 3) {
            blockpos = blockpos.offset(random.nextInt(-10, 10), 0, random.nextInt(-10, 10));
            addHill(level, blockpos, random, height / count);
            count++;
        }

        return true;
    }

    private void addHill(WorldGenLevel level, BlockPos blockpos, RandomSource random, int height) {
        // 地面を探す
        while (level.isEmptyBlock(blockpos) && blockpos.getY() > level.getMinBuildHeight() + 2) {
            blockpos = blockpos.below();
        }

        if (!level.getBlockState(blockpos).isCollisionShapeFullBlock(level, blockpos)) {
            return;
        }

        blockpos = blockpos.above(random.nextInt(4));
        int j = height / 4 + random.nextInt(2);
        float v = 1.0F + random.nextFloat() * (30 - height) / 30;

        // --- 歪み用パラメータ ---
        float scaleX = 0.6f + random.nextFloat() * 0.8f; // 楕円率X
        float scaleZ = 0.6f + random.nextFloat() * 0.8f; // 楕円率Z
        int offsetX = random.nextInt(3) - 1;             // -1～1
        int offsetZ = random.nextInt(3) - 1;
        float offsetMul = random.nextFloat();
        double angle = Math.toRadians(random.nextInt(360));
        double hillRot = Math.toRadians(random.nextInt(360));

        // 噴出の柱部分
        for (int k = 0; k < height; k++) {
            float f = (v - (float) k / (float) height) * (float) j;
            int l = Mth.ceil(f);

            for (int dx = -l; dx <= l; dx++) {

                for (int dz = -l; dz <= l; dz++) {
                    float f1 = (float) Mth.abs((float) (Math.cos(hillRot) * dx - Math.sin(hillRot) * dz)) - 0.25F;
                    float f2 = (float) Mth.abs((float) (Math.sin(hillRot) * dx + Math.cos(hillRot) * dz)) - 0.25F;

                    // --- 楕円形の判定 + 中心のずらし ---
                    float dist = (f1 * f1) / (scaleX * scaleX) + (f2 * f2) / (scaleZ * scaleZ);
                    if (dist <= f * f) {
                        BlockPos target = blockpos.offset((int) (dx + offsetX * f * offsetMul), k, (int) (dz + offsetZ * f * offsetMul));
                        BlockState below = level.getBlockState(target.below());

                        if (below.is(BlockInit.SCORCHED_GRASS_SOIL.get())) {
                            this.setBlock(level, target.below(), BlockInit.SCORCHED_SOIL.get().defaultBlockState());
                        }
                        this.setBlock(level, target, BlockInit.SCORCHED_GRASS_SOIL.get().defaultBlockState());
                    }
                }
            }
        }

        for (int k = 0; k < height; k++) {
            float f = (v - (float) k / (float) height) * (float) j;
            int l = Mth.ceil(f);

            if (l > 2 && k % 2 == 1) {
                BlockPos pos = blockpos.offset((int) (offsetX * f * offsetMul), k, (int) (offsetZ * f * offsetMul))
                        .offset((int) (Math.cos(angle) * f * Mth.randomBetween(random, 0.8F, 1.3F)), 0, (int) (Math.sin(angle) * f * Mth.randomBetween(random, 0.8F, 1.3F)));
                addPool(level, pos, random, l * 2 / 3);
                angle += Math.toRadians(random.nextInt(60, 120));
            }
        }

        // 下を埋める
        int k1 = Mth.ceil(v * j);
        if (k1 < 0) k1 = 0;

        for (int dx = -k1; dx <= k1; dx++) {
            for (int dz = -k1; dz <= k1; dz++) {
                BlockPos blockpos1 = blockpos.offset((int) (dx + offsetX * k1 * offsetMul), -1, (int) (dz + offsetZ * k1 * offsetMul));
                int depthCounter = 10;

                while (blockpos1.getY() > 50) {
                    BlockState state = level.getBlockState(blockpos1);
                    if (!state.isAir() && !isDirt(state) && state.getFluidState().isEmpty()) break;
                    float f1 = (float) (Math.cos(hillRot) * dx - Math.sin(hillRot) * dz);
                    float f2 = (float) (Math.sin(hillRot) * dx + Math.cos(hillRot) * dz);

                    // --- 楕円形の判定 + 中心のずらし ---
                    float dist = (f1 * f1) / (scaleX * scaleX) + (f2 * f2) / (scaleZ * scaleZ);
                    if (dist <= k1 * k1) {
                        BlockState above = level.getBlockState(blockpos1.above());
                        if (above.isAir()) {
                            this.setBlock(level, blockpos1, BlockInit.SCORCHED_GRASS_SOIL.get().defaultBlockState());
                        } else if (level.getBlockState(blockpos1.above(2)).isAir() || level.getBlockState(blockpos1.above(3)).isAir()) {
                            this.setBlock(level, blockpos1, BlockInit.SCORCHED_SOIL.get().defaultBlockState());
                        }
                    }

                    blockpos1 = blockpos1.below();
                    if (--depthCounter <= 0) {
                        blockpos1 = blockpos1.below(random.nextInt(5) + 1);
                        depthCounter = random.nextInt(5);
                    }
                }
            }
        }
    }

    private void addPool(WorldGenLevel level, BlockPos blockpos, RandomSource random, int radius) {
        // 半径をランダムで決定（くぼみのサイズ）
        int radiusX = (int) (radius * Mth.randomBetween(random, 0.9F, 1.1F)); // X方向の半径
        int radiusZ = (int) (radius * Mth.randomBetween(random, 0.9F, 1.1F)); // Z方向の半径
        int radiusY = (int) (radius * Mth.randomBetween(random, 0.9F, 1.1F)); // 深さ（Y方向）
        double poolRot = Math.toRadians(random.nextInt(360));

        for (int dx = -radiusX - 5; dx <= radiusX + 5; dx++) {
            for (int dz = -radiusZ - 5; dz <= radiusZ + 5; dz++) {
                for (int dy = -radiusY - 5; dy <= 2; dy++) {
                    double nx = (float) (Math.cos(poolRot) * dx - Math.sin(poolRot) * dz) / (double) radiusX;
                    double nz = (float) (Math.sin(poolRot) * dx + Math.cos(poolRot) * dz) / (double) radiusZ;
                    double ny = dy / (double) radiusY;

                    // --- 楕円形の判定 + 中心のずらし ---
                    double distSq = nx * nx + ny * ny + nz * nz;
                    double dist = Math.sqrt(distSq);

                    BlockPos pos = blockpos.offset(dx, dy, dz);

                    if (dist <= 0.9) {
                        if (dy <= 0) {
                            // 内部 → 液体
                            setBlock(level, pos, Blocks.LAVA.defaultBlockState());
                            if (level.getBlockState(pos.offset(0, -1, 0)).is(BlockInit.SCORCHED_GRASS_SOIL.get())) {
                                setBlock(level, pos.offset(0, -1, 0), BlockInit.SCORCHED_SOIL.get().defaultBlockState());
                            }
                        } else {
                            setBlock(level, pos, Blocks.AIR.defaultBlockState());
                        }
                        List<Direction> directions = new java.util.ArrayList<>(Arrays.stream(Direction.values().clone()).toList());
                        directions.remove(Direction.UP);
                        for (Direction direction : directions) {
                            BlockPos pos1 = pos.relative(direction);
                            BlockState blockState = level.getBlockState(pos1);
                            if (!blockState.isCollisionShapeFullBlock(level, pos1) && !blockState.is(Blocks.LAVA)) {
                                if (dy + direction.getStepY() < 0) {
                                    setBlock(level, pos1, BlockInit.SCORCHED_SOIL.get().defaultBlockState());
                                    if (level.getBlockState(pos1.offset(0, -1, 0)).is(BlockInit.SCORCHED_GRASS_SOIL.get())) {
                                        setBlock(level, pos1.offset(0, -1, 0), BlockInit.SCORCHED_SOIL.get().defaultBlockState());
                                    }
                                } else if (dy + direction.getStepY() == 0 && level.getBlockState(pos1.above()).isAir()) {
                                    if (random.nextFloat() < 0.75F) {
                                        setBlock(level, pos1, BlockInit.SCORCHED_GRASS_SOIL.get().defaultBlockState());
                                    } else {
                                        int i;
                                        for (i = 0; i < random.nextInt(1, 3); i++) {
                                            setBlock(level, pos1.offset(0, -i, 0), BlockInit.SULFUR_BLOCK.get().defaultBlockState());
                                            for (Direction direction1 : Direction.values()) {
                                                BlockPos pos2 = pos1.relative(direction1);
                                                if (level.getBlockState(pos2).isAir() && random.nextFloat() < 0.15F) {
                                                    setBlock(level, pos2, BlockInit.SULFUR_CRYSTAL_CLUSTER.get().defaultBlockState().setValue(CrystalClusterBlock.FACING, direction1));
                                                }
                                            }
                                        }
                                        if (level.getBlockState(pos1.offset(0, -i, 0)).isAir()) {
                                            setBlock(level, pos1.offset(0, -i, 0), BlockInit.SULFUR_CRYSTAL_CLUSTER.get().defaultBlockState().setValue(CrystalClusterBlock.FACING, Direction.DOWN));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}