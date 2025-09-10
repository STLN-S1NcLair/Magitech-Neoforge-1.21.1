package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record RangedEntityAttackPayload(int id, int targetId, float distanceFactor, int index) implements CustomPacketPayload {
    public static final ResourceLocation RANGED_ENTITY_ATTACK = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ranged_entity_attack");
    public static final Type<RangedEntityAttackPayload> TYPE = new Type<>(RANGED_ENTITY_ATTACK);
    public static final StreamCodec<ByteBuf, RangedEntityAttackPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            RangedEntityAttackPayload::id,
            ByteBufCodecs.INT,
            RangedEntityAttackPayload::targetId,
            ByteBufCodecs.FLOAT,
            RangedEntityAttackPayload::distanceFactor,
            ByteBufCodecs.INT,
            RangedEntityAttackPayload::index,
            RangedEntityAttackPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
