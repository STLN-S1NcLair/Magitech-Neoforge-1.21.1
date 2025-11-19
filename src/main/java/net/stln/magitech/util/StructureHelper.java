package net.stln.magitech.util;

import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StructureHelper {

    public static boolean matchesStructure(Level level, BlockPos origin, Map<BlockPos, BlockState> structure, Vec3i size, Function<BlockState, Boolean> voidFilter) {
        for (int i = 0; i < size.getX(); i++) {
            for (int j = 0; j < size.getY(); j++) {
                for (int k = 0; k < size.getZ(); k++) {
                    BlockState target = level.getBlockState(origin.offset(i, j, k));
                    BlockState state = structure.get(new BlockPos(i, j, k));
                    if ((!structure.containsKey(new BlockPos(i, j, k)) && voidFilter.apply(target)) || (structure.containsKey(new BlockPos(i, j, k)) && (target.equals(state) || (target.getBlock().equals(state.getBlock()) && !state.hasProperty(BlockStateProperties.FACING))))) {

                        continue;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static BlockPos detectStructureError(Level level, BlockPos origin, Map<BlockPos, BlockState> structure, Vec3i size, Function<BlockState, Boolean> voidFilter) {
        for (int i = 0; i < size.getX(); i++) {
            for (int j = 0; j < size.getY(); j++) {
                for (int k = 0; k < size.getZ(); k++) {
                    BlockState target = level.getBlockState(origin.offset(i, j, k));
                    if ((!structure.containsKey(new BlockPos(i, j, k)) && voidFilter.apply(target)) || target.equals(structure.get(new BlockPos(i, j, k)))) {
                        continue;
                    } else {
                        return origin.offset(i, j, k);
                    }
                }
            }
        }
        return null;
    }

    public static boolean checkMultiblock(Level level, ResourceLocation resourceLocation, BlockPos origin) {
        Multiblock multiblock = MultiblockDataManager.get().getMultiblock(resourceLocation);
        return checkMultiblock(level, multiblock, origin);
    }

    public static boolean checkMultiblock(Level level, Multiblock multiblock, BlockPos origin) {

        if (multiblock == null) {
            return false; // 定義が見つからない
        }

        // 構造が一致しているか判定（originを基準点として）
        return multiblock.validate(level, origin) != null;
    }

    public static BlockPos detectMultiblockError(Level level, ResourceLocation resourceLocation, BlockPos origin, Rotation rotation, boolean mirror) {
        Multiblock multiblock = MultiblockDataManager.get().getMultiblock(resourceLocation);

        return detectMultiblockError(level, multiblock, origin, rotation, mirror);
    }

    public static BlockPos detectMultiblockError(Level level, Multiblock multiblock, BlockPos origin, Rotation rotation, boolean mirror) {

        if (multiblock == null) {
            return null; // 定義が見つからない
        }

        Pair<BlockPos, Collection<Multiblock.SimulateResult>> result = multiblock.simulate(level, origin, rotation, mirror, false);

// 不適合な座標だけ抽出
        List<BlockPos> mismatchedPositions = result.getSecond().stream()
                .filter(r -> !r.test(level, rotation))
                .map(Multiblock.SimulateResult::getWorldPosition)
                .toList();
        return mismatchedPositions.stream()
                .findFirst()
                .orElse(null); // 最初の不適合な座標を返す
    }
}
