package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record OpenThreadBoundPageScreenPayload(String uuid) implements CustomPacketPayload {
    public static final ResourceLocation open_thread_bound_page_screen_C2S_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "open_thread_bound_page_screen");
    public static final Type<OpenThreadBoundPageScreenPayload> TYPE = new Type<>(open_thread_bound_page_screen_C2S_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, OpenThreadBoundPageScreenPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            OpenThreadBoundPageScreenPayload::uuid,
            OpenThreadBoundPageScreenPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
