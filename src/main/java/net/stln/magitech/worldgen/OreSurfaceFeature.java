package net.stln.magitech.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;

public class OreSurfaceFeature extends Feature<NoneFeatureConfiguration> {
    private final BlockState blockToPlace;
    private final List<Block> blockToPlaced;
    private final double propability;

    public OreSurfaceFeature(BlockState blockToPlace, List<Block> blockToPlaced, double propability) {
        super(NoneFeatureConfiguration.CODEC);
        this.blockToPlace = blockToPlace;
        this.blockToPlaced = blockToPlaced;
        this.propability = propability;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        ChunkPos chunkPos = new ChunkPos(context.origin());
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                for (int dy = level.getMinBuildHeight(); dy < level.getMaxBuildHeight(); dy++) {
                    pos.set(chunkPos.getMinBlockX() + dx, dy, chunkPos.getMinBlockZ() + dz);
                    BlockState state = level.getBlockState(pos);

                    boolean isTargetBlock = false;
                    for (Block block : blockToPlaced) {
                        if (state.is(block)) {
                            isTargetBlock = true;
                        }
                    }
                    if (isTargetBlock) {
                        for (Direction dir : Direction.values()) {
                            BlockPos facePos = pos.relative(dir);
                            if ((level.getBlockState(facePos).isAir() || level.getBlockState(facePos).is(Blocks.WATER)) && level.getRandom().nextFloat() < propability) {
                                BlockState blockState = blockToPlace;
                                if (blockToPlace.hasProperty(BlockStateProperties.FACING)) {
                                    blockState = blockState.setValue(BlockStateProperties.FACING, dir);
                                }
                                if (level.getBlockState(facePos).is(Blocks.WATER) && blockToPlace.hasProperty(BlockStateProperties.FACING)) {
                                    blockState = blockState.setValue(BlockStateProperties.WATERLOGGED, true);
                                }
                                level.setBlock(facePos, blockState, 2);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
