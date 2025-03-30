package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

import java.util.UUID;

public record LeftClickC2SPayload(int clickCount, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation LEFT_CLICK_C2S_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "left_click");
    public static final CustomPacketPayload.Type<LeftClickC2SPayload> TYPE = new CustomPacketPayload.Type<>(LEFT_CLICK_C2S_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, LeftClickC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            LeftClickC2SPayload::clickCount,
            ByteBufCodecs.STRING_UTF8,
            LeftClickC2SPayload::uuid,
            LeftClickC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
