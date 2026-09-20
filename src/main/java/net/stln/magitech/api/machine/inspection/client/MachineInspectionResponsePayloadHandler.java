package net.stln.magitech.api.machine.inspection.client;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.api.machine.inspection.network.MachineInspectionResponsePayload;

public final class MachineInspectionResponsePayloadHandler {
    private MachineInspectionResponsePayloadHandler() {
    }

    public static void handle(MachineInspectionResponsePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> MachineInspectionClient.accept(payload.data()));
    }
}
