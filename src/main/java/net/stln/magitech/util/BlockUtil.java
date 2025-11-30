package net.stln.magitech.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class BlockUtil {

    public static Set<BlockPos> getConnectedBlocks(Level level, BlockPos startPos, Block targetBlock, int limit) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> result = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty() && result.size() < limit + 1) {
            BlockPos current = queue.poll();

            if (level.getBlockState(current).getBlock() == targetBlock) {
                result.add(current);
            }

            // センターチェック
            for (Direction direction : Direction.values()) {
                if (!isInLimit(visited, limit)) continue;
                BlockPos neighbor = current.relative(direction);
                if (!visited.contains(neighbor) && level.getBlockState(neighbor).getBlock() == targetBlock) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }


            // エッジチェック
            for (Direction direction : Direction.values()) {
                for (Direction direction2 : Direction.values()) {
                    if (!isInLimit(visited, limit)) continue;
                    if (direction2.getAxis() == direction.getAxis()) continue;
                    BlockPos neighbor = current.relative(direction).relative(direction2);
                    if (!visited.contains(neighbor) && level.getBlockState(neighbor).getBlock() == targetBlock) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }


            // コーナーチェック
            for (Direction direction : Direction.values()) {
                for (Direction direction2 : Direction.values()) {
                    for (Direction direction3 : Direction.values()) {
                        if (!isInLimit(visited, limit)) continue;
                        if (direction2.getAxis() == direction.getAxis()
                                || direction3.getAxis() == direction.getAxis()
                                || direction3.getAxis() == direction2.getAxis()) continue;
                        BlockPos neighbor = current.relative(direction);
                        if (!visited.contains(neighbor) && level.getBlockState(neighbor).getBlock() == targetBlock) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }

        return result;
    }

    private static boolean isInLimit(Set<BlockPos> posSet, int limit) {
        return posSet.size() < limit;
    }
}
