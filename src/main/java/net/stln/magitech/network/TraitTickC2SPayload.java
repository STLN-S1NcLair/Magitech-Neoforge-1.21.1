package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record TraitTickC2SPayload(boolean isMainHand, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation TRAIT_TICK_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "trait_tick");
    public static final Type<TraitTickC2SPayload> TYPE = new Type<>(TRAIT_TICK_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, TraitTickC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            TraitTickC2SPayload::isMainHand,
            ByteBufCodecs.STRING_UTF8,
            TraitTickC2SPayload::uuid,
            TraitTickC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
