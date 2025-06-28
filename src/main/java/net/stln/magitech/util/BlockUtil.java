package net.stln.magitech.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.*;

public class BlockUtil {

    public static Set<BlockPos> getConnectedBlocks(Level level, BlockPos startPos, Block targetBlock, int limit) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> result = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty() && result.size() < limit) {
            BlockPos current = queue.poll();

            if (level.getBlockState(current).getBlock() == targetBlock) {
                result.add(current);
            }

            // 3x3x3の近傍をチェック
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        BlockPos neighbor = current.offset(dx, dy, dz);
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
}
