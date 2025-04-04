package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record BreakBlockS2CPayload(BlockPos pos, BlockPos initialPos, String uuid, boolean effect) implements CustomPacketPayload {
    public static final ResourceLocation BREAK_BLOCK_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "break_block");
    public static final Type<BreakBlockS2CPayload> TYPE = new Type<>(BREAK_BLOCK_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, BreakBlockS2CPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            BreakBlockS2CPayload::pos,
            BlockPos.STREAM_CODEC,
            BreakBlockS2CPayload::initialPos,
            ByteBufCodecs.STRING_UTF8,
            BreakBlockS2CPayload::uuid,
            ByteBufCodecs.BOOL,
            BreakBlockS2CPayload::effect,
            BreakBlockS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
