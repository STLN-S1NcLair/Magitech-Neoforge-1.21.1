package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record LeftClickPayload(int clickCount, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation LEFT_CLICK_C2S_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "left_click");
    public static final CustomPacketPayload.Type<LeftClickPayload> TYPE = new CustomPacketPayload.Type<>(LEFT_CLICK_C2S_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, LeftClickPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            LeftClickPayload::clickCount,
            ByteBufCodecs.STRING_UTF8,
            LeftClickPayload::uuid,
            LeftClickPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
