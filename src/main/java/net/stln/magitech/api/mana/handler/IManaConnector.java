package net.stln.magitech.api.mana.handler;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IManaConnector {
    // マナコンテナを接続するブロックのインターフェース
    default boolean canConnect(Direction direction, BlockState state) {
        return true;
    }
}
