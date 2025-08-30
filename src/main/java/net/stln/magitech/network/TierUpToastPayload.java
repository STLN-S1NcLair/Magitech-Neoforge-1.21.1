package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record TierUpToastPayload(int slot, int newTier, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation TIER_UP_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "tier_up_toast");
    public static final Type<TierUpToastPayload> TYPE = new Type<>(TIER_UP_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, TierUpToastPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            TierUpToastPayload::slot,
            ByteBufCodecs.INT,
            TierUpToastPayload::newTier,
            ByteBufCodecs.STRING_UTF8,
            TierUpToastPayload::uuid,
            TierUpToastPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
