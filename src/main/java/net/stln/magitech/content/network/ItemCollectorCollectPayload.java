package net.stln.magitech.content.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public record ItemCollectorCollectPayload(BlockPos blockPos, Vector3f pos) implements CustomPacketPayload {
    public static final ResourceLocation ID = Magitech.id("item_collector_collect");
    public static final Type<ItemCollectorCollectPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<ByteBuf, ItemCollectorCollectPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ItemCollectorCollectPayload::blockPos,
            ByteBufCodecs.VECTOR3F,
            ItemCollectorCollectPayload::pos,
            ItemCollectorCollectPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
