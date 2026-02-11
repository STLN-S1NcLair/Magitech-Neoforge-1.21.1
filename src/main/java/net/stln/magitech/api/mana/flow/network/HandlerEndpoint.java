package net.stln.magitech.api.mana.flow.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record HandlerEndpoint(BlockPos pos, Direction direction) {
}
