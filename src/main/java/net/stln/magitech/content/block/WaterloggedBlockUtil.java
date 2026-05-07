package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

/**
 * SimpleWaterloggedBlock 向けの共通処理。
 */
public final class WaterloggedBlockUtil {
    private WaterloggedBlockUtil() {}

    public static boolean isWaterAtPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return fluidstate.getType() == Fluids.WATER;
    }

    public static BlockState setWaterlogged(BlockState state, BooleanProperty waterlogged, boolean value) {
        return state.setValue(waterlogged, value);
    }

    public static void scheduleWaterTickIfNeeded(BlockState state, BooleanProperty waterlogged, LevelAccessor level, BlockPos pos) {
        if (state.getValue(waterlogged)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
    }
}
