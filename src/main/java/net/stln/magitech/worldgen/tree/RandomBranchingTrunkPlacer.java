package net.stln.magitech.worldgen.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class RandomBranchingTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<RandomBranchingTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            p_70261_ -> trunkPlacerParts(p_70261_).apply(p_70261_, RandomBranchingTrunkPlacer::new)
    );


    private final int branchLength;

    public RandomBranchingTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
        this.branchLength = baseHeight / 3;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TreeGrowerInit.RANDOM_BRANCHING_TRUNK_PLACER.get(); // 登録が必要
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(
            LevelSimulatedReader reader,
            BiConsumer<BlockPos, BlockState> replacer,
            RandomSource random,
            int height,
            BlockPos startPos,
            TreeConfiguration config
    ) {
        List<FoliagePlacer.FoliageAttachment> foliage = new ArrayList<>();
        BlockPos.MutableBlockPos pos = startPos.mutable();
        Direction[] directions = Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new);

        int mid = height / 3;
        Direction prevBranchDir = null;
        int baseBranchLength = 3;
        for (int y = 0; y < height; y++) {
            // 幹を置く
            placeLog(reader, replacer, random, pos, config);

            if (y >= mid && (y - mid) % 2 == 0 && y < height - 1) {
                // 枝方向を決定
                Direction branchDir = directions[random.nextInt(directions.length)];
                if (branchDir == prevBranchDir) {
                    branchDir = branchDir.getClockWise();
                }

                // 枝を生成
                int length = baseBranchLength + random.nextInt(branchLength);
                baseBranchLength--;
                BlockPos branchStart = pos.immutable();
                BlockPos.MutableBlockPos branchPos = branchStart.mutable();

                for (int i = 0; i < length; i++) {
                    branchPos.move(branchDir);
                    placeLog(reader, replacer, random, branchPos, config, branchDir.getAxis());

                    if (random.nextBoolean() && i > 0) {
                        branchPos.move(Direction.UP);
                    }
                }

                foliage.add(new FoliagePlacer.FoliageAttachment(branchPos.immutable(), 0, false));
                prevBranchDir = branchDir;

                if (random.nextBoolean()) {
                    pos.move(branchDir.getOpposite());
                }
            }

            pos.move(Direction.UP);
        }

        foliage.add(new FoliagePlacer.FoliageAttachment(pos.immutable(), 0, false));
        return foliage;
    }

    private void placeLog(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, BlockPos pos, TreeConfiguration config, Direction.Axis axisDir) {
        BlockState log = config.trunkProvider.getState(random, pos).setValue(RotatedPillarBlock.AXIS, axisDir);
        replacer.accept(pos, log);
    }

    protected boolean placeLog(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, BlockPos pos, TreeConfiguration config) {
        placeLog(reader, replacer, random, pos, config, Direction.Axis.Y);
        return false;
    }
}