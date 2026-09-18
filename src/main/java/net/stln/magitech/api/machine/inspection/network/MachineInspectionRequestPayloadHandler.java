package net.stln.magitech.api.machine.inspection.network;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.api.machine.inspection.MachineInspectionApi;
import net.stln.magitech.api.machine.inspection.MachineInspectionData;

public final class MachineInspectionRequestPayloadHandler {
    private static final double MAX_INSPECTION_DISTANCE = 8.0D;

    private MachineInspectionRequestPayloadHandler() {
    }

    public static void handle(MachineInspectionRequestPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }
        context.enqueueWork(() -> {
            if (!isValidTarget(player, payload)) {
                return;
            }
            MachineInspectionData data = MachineInspectionApi.collect(player, payload.targetPosition());
            if (data != null) {
                PacketDistributor.sendToPlayer(player, new MachineInspectionResponsePayload(data));
            }
        });
    }

    private static boolean isValidTarget(ServerPlayer player, MachineInspectionRequestPayload payload) {
        if (player.distanceToSqr(payload.targetPosition().getCenter()) > MAX_INSPECTION_DISTANCE * MAX_INSPECTION_DISTANCE) {
            return false;
        }
        HitResult hitResult = player.pick(MAX_INSPECTION_DISTANCE, 0.0F, false);
        return hitResult instanceof BlockHitResult blockHitResult
                && blockHitResult.getBlockPos().equals(payload.targetPosition());
    }
}
