package net.stln.magitech.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockManaHandler extends IManaHandler {

    // 受け取り方向、自身の位置、自身のブロック状態
    boolean canReceiveMana(Direction direction, BlockPos pos, BlockState state);

    // 受け取り方向、自身の位置、自身のブロック状態
    boolean canExtractMana(Direction direction, BlockPos pos, BlockState state);
}
