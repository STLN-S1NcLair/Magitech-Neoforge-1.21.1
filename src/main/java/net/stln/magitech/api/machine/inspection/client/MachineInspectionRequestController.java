package net.stln.magitech.api.machine.inspection.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.api.machine.inspection.IMachineInspectionTarget;
import net.stln.magitech.api.machine.inspection.MachineInspectionApi;
import net.stln.magitech.api.machine.inspection.network.MachineInspectionRequestPayload;

/**
 * クライアントの視線対象を管理し、検査要求を必要な間隔で送信します。
 * Tracks the client look target and sends inspection requests at a bounded interval.
 */
public final class MachineInspectionRequestController {
    private MachineInspectionRequestController() {
    }

    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        if (!isInspectionAvailable(minecraft)) {
            clearAndTick();
            return;
        }

        if (!(minecraft.hitResult instanceof BlockHitResult blockHitResult)) {
            clearAndTick();
            return;
        }

        BlockPos targetPosition = blockHitResult.getBlockPos();
        BlockPos displayPosition = resolveDisplayPosition(minecraft.level, targetPosition);
        MachineInspectionClient.updateTarget(targetPosition, displayPosition);
        // この要求は毎tick必要です。検査対象の進捗・流量・インベントリを最新状態で表示するため、頻度を下げないでください。
        // This request must be sent every tick. Do not reduce its frequency; the HUD must show the latest progress, flow, and inventory state.
        PacketDistributor.sendToServer(new MachineInspectionRequestPayload(targetPosition));
        MachineInspectionClient.tick();
    }

    private static boolean isInspectionAvailable(Minecraft minecraft) {
        return minecraft.player != null
                && minecraft.level != null
                && minecraft.screen == null
                && !minecraft.options.hideGui
                && MachineInspectionApi.canDisplay(minecraft.player);
    }

    private static void clearAndTick() {
        MachineInspectionClient.clear();
        MachineInspectionClient.tick();
    }

    private static BlockPos resolveDisplayPosition(Level level, BlockPos targetPosition) {
        if (level.getBlockEntity(targetPosition) instanceof IMachineInspectionTarget inspectionTarget) {
            BlockPos displayPosition = inspectionTarget.getInspectionPosition();
            if (displayPosition != null) {
                return displayPosition;
            }
        }
        return targetPosition;
    }
}
