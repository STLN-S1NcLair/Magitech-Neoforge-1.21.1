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

public record SwapToolFromBeltPayload(int select, UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation SWAP_TOOL_FROM_BELT_PAYLOAD_ID = Magitech.id("swap_tool_from_belt");
    public static final Type<SwapToolFromBeltPayload> TYPE = new Type<>(SWAP_TOOL_FROM_BELT_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, SwapToolFromBeltPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SwapToolFromBeltPayload::select,
            UUIDUtil.STREAM_CODEC,
            SwapToolFromBeltPayload::uuid,
            SwapToolFromBeltPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
