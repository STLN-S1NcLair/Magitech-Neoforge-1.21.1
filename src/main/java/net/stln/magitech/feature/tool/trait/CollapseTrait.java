package net.stln.magitech.feature.tool.trait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.tool.property.ToolProperties;
import net.stln.magitech.feature.tool.property.ToolPropertyCategory;
import net.stln.magitech.feature.tool.property.modifier.RationalToolPropertyModifier;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.helper.BlockHelper;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class CollapseTrait extends Trait {

    @Override
    public float modifyMiningSpeed(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos) {
        return isWideEnough(level, blockState, pos) ? 0.2F : super.modifyMiningSpeed(player, level, stack, traitLevel, properties, blockState, pos);
    }

    @Override
    public void additionalBlockBreak(Player player, Level level, ItemStack stack, int traitLevel, ToolProperties properties, BlockState blockState, BlockPos pos, Set<BlockPos> posSet, int damageAmount, Direction direction, boolean simulate) {
        boolean isWideEnough = isWideEnough(level, blockState, pos);
        if (isWideEnough) {
            posSet.addAll(BlockHelper.getConnectedBlocks(level, pos, blockState.getBlock(), traitLevel * 15));
        }
        super.additionalBlockBreak(player, level, stack, traitLevel, properties, blockState, pos, posSet, damageAmount, direction, simulate);
    }

    private static boolean isWideEnough(Level level, BlockState blockState, BlockPos pos) {
        int width = 1;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos offsetPos = pos.relative(direction);
            if (level.getBlockState(offsetPos).getBlock().equals(blockState.getBlock())) {
                width++;
            }
        }
        return width >= 3;
    }

    @Override
    public Color getColor() {
        return new Color(0x9797AF);
    }

    @Override
    public Color getPrimary() {
        return new Color(0xD2D5FF);
    }

    @Override
    public Color getSecondary() {
        return new Color(0x827A75);
    }

    @Override
    public ResourceLocation getKey() {
        return Magitech.id("collapse");
    }

}
