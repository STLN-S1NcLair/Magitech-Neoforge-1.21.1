package net.stln.magitech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.mana.ManaUtil;

public record SyncManaPayload(double value, int manaType, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation SYNC_MANA_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "sync_mana");
    public static final Type<SyncManaPayload> TYPE = new Type<>(SYNC_MANA_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, SyncManaPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            SyncManaPayload::value,
            ByteBufCodecs.INT,
            SyncManaPayload::manaType,
            ByteBufCodecs.STRING_UTF8,
            SyncManaPayload::uuid,
            SyncManaPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
