package net.stln.magitech.api.machine.inspection.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.machine.inspection.MachineInspectionData;

public record MachineInspectionResponsePayload(MachineInspectionData data) implements CustomPacketPayload {
    public static final Type<MachineInspectionResponsePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "machine_inspection_response")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MachineInspectionResponsePayload> STREAM_CODEC =
            MachineInspectionData.STREAM_CODEC.map(
                    MachineInspectionResponsePayload::new,
                    MachineInspectionResponsePayload::data
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
