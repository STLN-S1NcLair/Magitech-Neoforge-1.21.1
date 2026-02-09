package net.stln.magitech.api.mana;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface IManaNode {
    /**
     * 周囲の接続状況を再確認するよう要求する
     */
    void requestRescan();
}
