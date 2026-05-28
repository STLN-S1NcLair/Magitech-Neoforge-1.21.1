package net.stln.magitech.content.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record CompressorCraftPayload(BlockPos pos) implements CustomPacketPayload {

    public static final Type<CompressorCraftPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "compressor_craft"));

    // データの読み書き定義 (BlockPos 2つだけなので非常に軽い)
    public static final StreamCodec<ByteBuf, CompressorCraftPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, CompressorCraftPayload::pos,
            CompressorCraftPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
