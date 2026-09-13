package net.stln.magitech.core.api.mana.flow.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/**
 * マナハンドラーのワールド位置と接続面を表します。
 * Represents a mana handler's world position and connection side.
 */
public record HandlerEndpoint(BlockPos pos, Direction direction) {
}
