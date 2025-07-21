package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record LongJumpPayload(int jumpCount, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation LONG_JUMP_C2S_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "long_jump");
    public static final Type<LongJumpPayload> TYPE = new Type<>(LONG_JUMP_C2S_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, LongJumpPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            LongJumpPayload::jumpCount,
            ByteBufCodecs.STRING_UTF8,
            LongJumpPayload::uuid,
            LongJumpPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
