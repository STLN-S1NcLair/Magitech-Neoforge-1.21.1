package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record ManaNodeTransferPayload(BlockPos from, BlockPos to) implements CustomPacketPayload {

    public static final Type<ManaNodeTransferPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "mana_node_transfer"));

    // データの読み書き定義 (BlockPos 2つだけなので非常に軽い)
    public static final StreamCodec<ByteBuf, ManaNodeTransferPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ManaNodeTransferPayload::from,
            BlockPos.STREAM_CODEC, ManaNodeTransferPayload::to,
            ManaNodeTransferPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
