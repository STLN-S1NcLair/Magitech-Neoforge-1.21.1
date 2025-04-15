package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record TraitTickPayload(boolean isMainHand, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation TRAIT_TICK_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "trait_tick");
    public static final Type<TraitTickPayload> TYPE = new Type<>(TRAIT_TICK_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, TraitTickPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            TraitTickPayload::isMainHand,
            ByteBufCodecs.STRING_UTF8,
            TraitTickPayload::uuid,
            TraitTickPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
