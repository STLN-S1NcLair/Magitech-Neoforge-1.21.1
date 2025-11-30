package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record  ThreadboundSelectPayload(int select, UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation THREADBOUND_SELECT_PAYLOAD_ID = Magitech.id("threadbound_select");
    public static final Type<ThreadboundSelectPayload> TYPE = new Type<>(THREADBOUND_SELECT_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, ThreadboundSelectPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ThreadboundSelectPayload::select,
            UUIDUtil.STREAM_CODEC,
            ThreadboundSelectPayload::uuid,
            ThreadboundSelectPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
