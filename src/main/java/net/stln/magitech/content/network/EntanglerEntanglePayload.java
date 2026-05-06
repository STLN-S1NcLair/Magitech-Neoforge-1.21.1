package net.stln.magitech.content.network;

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

public record EntanglerEntanglePayload(BlockPos pos) implements CustomPacketPayload {
    public static final ResourceLocation ID = Magitech.id("entangler_entangle");
    public static final Type<EntanglerEntanglePayload> TYPE = new Type<>(ID);
    public static final StreamCodec<ByteBuf, EntanglerEntanglePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            EntanglerEntanglePayload::pos,
            EntanglerEntanglePayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
