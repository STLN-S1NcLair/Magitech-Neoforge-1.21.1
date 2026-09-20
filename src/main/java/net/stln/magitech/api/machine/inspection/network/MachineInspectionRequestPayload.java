package net.stln.magitech.api.machine.inspection.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record MachineInspectionRequestPayload(BlockPos targetPosition) implements CustomPacketPayload {
    public static final Type<MachineInspectionRequestPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "machine_inspection_request")
    );
    public static final StreamCodec<ByteBuf, MachineInspectionRequestPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            MachineInspectionRequestPayload::targetPosition,
            MachineInspectionRequestPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
