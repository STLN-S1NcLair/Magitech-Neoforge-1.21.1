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

public record LeftClickPayload(int clickCount, UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation LEFT_CLICK_C2S_PAYLOAD_ID = Magitech.id("left_click");
    public static final CustomPacketPayload.Type<LeftClickPayload> TYPE = new CustomPacketPayload.Type<>(LEFT_CLICK_C2S_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, LeftClickPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            LeftClickPayload::clickCount,
            UUIDUtil.STREAM_CODEC,
            LeftClickPayload::uuid,
            LeftClickPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
