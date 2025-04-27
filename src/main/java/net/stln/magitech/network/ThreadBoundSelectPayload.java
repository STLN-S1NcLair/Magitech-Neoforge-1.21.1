package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record ThreadBoundSelectPayload(int select, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation THREADBOUND_SELECT_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound_select");
    public static final Type<ThreadBoundSelectPayload> TYPE = new Type<>(THREADBOUND_SELECT_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, ThreadBoundSelectPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ThreadBoundSelectPayload::select,
            ByteBufCodecs.STRING_UTF8,
            ThreadBoundSelectPayload::uuid,
            ThreadBoundSelectPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
