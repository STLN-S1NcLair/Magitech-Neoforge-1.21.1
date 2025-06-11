package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;

public record TraitActionPayload(boolean isMainHand, int targetId, Vec3 targetPos,
                                 String uuid) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, Vec3> STREAM_VEC3 =
            StreamCodec.of(
                    (buf, vec) -> {
                        buf.writeDouble(vec.x);
                        buf.writeDouble(vec.y);
                        buf.writeDouble(vec.z);
                    },
                    buf -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble())
            );
    public static final ResourceLocation USE_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "use");
    public static final Type<TraitActionPayload> TYPE = new Type<>(USE_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, TraitActionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            TraitActionPayload::isMainHand,
            ByteBufCodecs.INT,
            TraitActionPayload::targetId,
            STREAM_VEC3,
            TraitActionPayload::targetPos,
            ByteBufCodecs.STRING_UTF8,
            TraitActionPayload::uuid,
            TraitActionPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
