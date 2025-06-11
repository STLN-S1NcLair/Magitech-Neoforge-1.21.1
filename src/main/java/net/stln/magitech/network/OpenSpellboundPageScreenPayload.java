package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;

public record OpenSpellboundPageScreenPayload(String uuid) implements CustomPacketPayload {
    public static final ResourceLocation OPEN_SPELLBOUND_PAGE_SCREEN_C2S_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "open_spellbound_page_screen");
    public static final Type<OpenSpellboundPageScreenPayload> TYPE = new Type<>(OPEN_SPELLBOUND_PAGE_SCREEN_C2S_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, OpenSpellboundPageScreenPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            OpenSpellboundPageScreenPayload::uuid,
            OpenSpellboundPageScreenPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
