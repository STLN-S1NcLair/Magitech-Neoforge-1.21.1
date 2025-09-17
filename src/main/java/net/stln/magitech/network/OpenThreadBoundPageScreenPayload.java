package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record OpenThreadBoundPageScreenPayload(UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation open_thread_bound_page_screen_C2S_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "open_thread_bound_page_screen");
    public static final Type<OpenThreadBoundPageScreenPayload> TYPE = new Type<>(open_thread_bound_page_screen_C2S_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, OpenThreadBoundPageScreenPayload> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            OpenThreadBoundPageScreenPayload::uuid,
            OpenThreadBoundPageScreenPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
