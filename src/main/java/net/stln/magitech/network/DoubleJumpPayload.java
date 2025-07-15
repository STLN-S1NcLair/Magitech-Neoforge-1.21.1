package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record DoubleJumpPayload(int jumpCount, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation DOUBLE_JUMP_C2S_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "double_jump");
    public static final Type<DoubleJumpPayload> TYPE = new Type<>(DOUBLE_JUMP_C2S_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, DoubleJumpPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            DoubleJumpPayload::jumpCount,
            ByteBufCodecs.STRING_UTF8,
            DoubleJumpPayload::uuid,
            DoubleJumpPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
