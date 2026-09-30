package net.stln.magitech.content.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.stln.magitech.Magitech;

public record CompressorAnimationPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<CompressorAnimationPayload> TYPE = new Type<>(Magitech.id("compressor_animation"));
    public static final StreamCodec<ByteBuf, CompressorAnimationPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CompressorAnimationPayload::pos,
            CompressorAnimationPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
