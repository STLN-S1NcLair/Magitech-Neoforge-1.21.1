package net.stln.magitech.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.stln.magitech.Magitech;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
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
}
