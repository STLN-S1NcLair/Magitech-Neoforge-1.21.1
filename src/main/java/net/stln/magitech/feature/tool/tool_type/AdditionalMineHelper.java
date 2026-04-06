package net.stln.magitech.feature.tool.tool_type;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.helper.BlockHelper;

import java.util.HashSet;
import java.util.Set;

public class AdditionalMineHelper {

    public static Set<BlockPos> addHammerMine(Player player, ItemStack stack, BlockPos pos, Direction direction) {
        SynthesisedToolItem item = (SynthesisedToolItem) stack.getItem();
        ToolProperties appliedProperties = item.getAppliedProperties(player, player.level(), stack);
        if (!(item).isCorrectTool(stack, player.level().getBlockState(pos), item, appliedProperties)) {
            return Set.of(pos);
        }
        Set<BlockPos> set = new HashSet<>();
        int x = 0;
        int y = 0;
        int z = 0;
        switch (direction) {
            case UP, DOWN -> {
                x = 1;
                z = 1;
            }
            case NORTH, SOUTH -> {
                x = 1;
                y = 1;
            }
            case EAST, WEST -> {
                y = 1;
                z = 1;
            }
        }
        for (int i = -x; i <= x; i++) {
            for (int j = -y; j <= y; j++) {
                for (int k = -z; k <= z; k++) {
                    if ((item).isCorrectTool(stack, player.level().getBlockState(pos.offset(i, j, k)), item, appliedProperties)) {
                        set.add(pos.offset(i, j, k));
                    }
                }
            }
        }
        return set;
    }

    public static Set<BlockPos> addScytheMine(Player player, ItemStack stack, BlockPos pos, Direction direction) {
        Level level = player.level();
        BlockState state = level.getBlockState(pos);
        boolean noCollision = state.getCollisionShape(level, pos).isEmpty();
        boolean instantBreak = state.getBlock().defaultDestroyTime() == 0.0f;
        if (noCollision && instantBreak) {
            return BlockHelper.getConnectedBlocks(player.level(), pos, state.getBlock(), 20);
        }
        return Set.of(pos);
    }
}
