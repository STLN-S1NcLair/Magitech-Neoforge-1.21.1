package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import net.stln.magitech.util.NetworkHelper;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record SparkTraitBeamPayload(Vec3 pos, Vec3 hitPos, UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation BREAK_BLOCK_PAYLOAD_ID = Magitech.id("spark_trait_beam");
    public static final Type<SparkTraitBeamPayload> TYPE = new Type<>(BREAK_BLOCK_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, SparkTraitBeamPayload> STREAM_CODEC = StreamCodec.composite(
            NetworkHelper.VEC3_STREAM_CODEC,
            SparkTraitBeamPayload::pos,
            NetworkHelper.VEC3_STREAM_CODEC,
            SparkTraitBeamPayload::hitPos,
            UUIDUtil.STREAM_CODEC,
            SparkTraitBeamPayload::uuid,
            SparkTraitBeamPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
