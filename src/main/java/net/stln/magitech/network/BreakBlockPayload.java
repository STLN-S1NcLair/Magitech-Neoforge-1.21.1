package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record BreakBlockPayload(BlockPos pos, BlockPos initialPos, UUID uuid, boolean effect, boolean callBreakBlock) implements CustomPacketPayload {
    public static final ResourceLocation BREAK_BLOCK_PAYLOAD_ID = Magitech.id("break_block");
    public static final Type<BreakBlockPayload> TYPE = new Type<>(BREAK_BLOCK_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, BreakBlockPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            BreakBlockPayload::pos,
            BlockPos.STREAM_CODEC,
            BreakBlockPayload::initialPos,
            UUIDUtil.STREAM_CODEC,
            BreakBlockPayload::uuid,
            ByteBufCodecs.BOOL,
            BreakBlockPayload::effect,
            ByteBufCodecs.BOOL,
            BreakBlockPayload::callBreakBlock,
            BreakBlockPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
