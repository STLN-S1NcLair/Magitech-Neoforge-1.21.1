package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record TraitTickPayload(boolean isMainHand, boolean isInventory, int slot,
                               UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation TRAIT_TICK_PAYLOAD_ID = Magitech.id("trait_tick");
    public static final Type<TraitTickPayload> TYPE = new Type<>(TRAIT_TICK_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, TraitTickPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            TraitTickPayload::isMainHand,
            ByteBufCodecs.BOOL,
            TraitTickPayload::isInventory,
            ByteBufCodecs.INT,
            TraitTickPayload::slot,
            UUIDUtil.STREAM_CODEC,
            TraitTickPayload::uuid,
            TraitTickPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
