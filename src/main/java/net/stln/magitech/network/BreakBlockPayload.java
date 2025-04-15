package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record BreakBlockPayload(BlockPos pos, BlockPos initialPos, String uuid, boolean effect) implements CustomPacketPayload {
    public static final ResourceLocation BREAK_BLOCK_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "break_block");
    public static final Type<BreakBlockPayload> TYPE = new Type<>(BREAK_BLOCK_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, BreakBlockPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            BreakBlockPayload::pos,
            BlockPos.STREAM_CODEC,
            BreakBlockPayload::initialPos,
            ByteBufCodecs.STRING_UTF8,
            BreakBlockPayload::uuid,
            ByteBufCodecs.BOOL,
            BreakBlockPayload::effect,
            BreakBlockPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
