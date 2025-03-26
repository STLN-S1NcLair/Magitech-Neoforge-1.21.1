package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record LeftClickC2SPayload(int clickCount) implements CustomPacketPayload {
    public static final ResourceLocation LEFT_CLICK_C2S_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "left_click");
    public static final CustomPacketPayload.Type<LeftClickC2SPayload> TYPE = new CustomPacketPayload.Type<>(LEFT_CLICK_C2S_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, LeftClickC2SPayload> STREAM_CODEC = ByteBufCodecs.INT.map(LeftClickC2SPayload::new, LeftClickC2SPayload::clickCount);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
