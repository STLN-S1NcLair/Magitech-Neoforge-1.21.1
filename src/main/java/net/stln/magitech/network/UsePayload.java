package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record UsePayload(boolean isMainHand, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation USE_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "use");
    public static final Type<UsePayload> TYPE = new Type<>(USE_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, UsePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            UsePayload::isMainHand,
            ByteBufCodecs.STRING_UTF8,
            UsePayload::uuid,
            UsePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
