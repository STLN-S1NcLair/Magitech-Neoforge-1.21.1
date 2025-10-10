package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

import java.util.UUID;

public record UseSpellPayload(boolean isMainHand, UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation USE_SPELL_PAYLOAD_ID = Magitech.id("use_spell");
    public static final Type<UseSpellPayload> TYPE = new Type<>(USE_SPELL_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, UseSpellPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            UseSpellPayload::isMainHand,
            UUIDUtil.STREAM_CODEC,
            UseSpellPayload::uuid,
            UseSpellPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
