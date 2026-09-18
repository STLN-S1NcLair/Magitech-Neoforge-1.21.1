package net.stln.magitech.api.machine.inspection;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.server.level.ServerPlayer;

public record MachineInspectionContext(
        ServerPlayer player,
        Level level,
        BlockPos targetPosition,
        BlockPos displayPosition,
        BlockEntity blockEntity
) {
}
