package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record ShootManaParcelTransferPayload(BlockPos from, Direction direction) implements CustomPacketPayload {

    public static final Type<ShootManaParcelTransferPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "shoot_mana_parcel"));

    public static final StreamCodec<ByteBuf, ShootManaParcelTransferPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ShootManaParcelTransferPayload::from,
            Direction.STREAM_CODEC, ShootManaParcelTransferPayload::direction,
            ShootManaParcelTransferPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
