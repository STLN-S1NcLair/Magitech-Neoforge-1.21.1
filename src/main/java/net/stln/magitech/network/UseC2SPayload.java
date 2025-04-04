package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record UseC2SPayload(boolean isMainHand, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation USE_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "use");
    public static final Type<UseC2SPayload> TYPE = new Type<>(USE_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, UseC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            UseC2SPayload::isMainHand,
            ByteBufCodecs.STRING_UTF8,
            UseC2SPayload::uuid,
            UseC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
