package net.stln.magitech.network;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ReleaseUsingSpellPayload(ItemStack stack, int chargeTime, UUID uuid) implements CustomPacketPayload {
    public static final ResourceLocation RELEASE_USING_SPELL_PAYLOAD_ID = Magitech.id("release_using_spell");
    public static final Type<ReleaseUsingSpellPayload> TYPE = new Type<>(RELEASE_USING_SPELL_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ReleaseUsingSpellPayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ReleaseUsingSpellPayload::stack,
            ByteBufCodecs.INT,
            ReleaseUsingSpellPayload::chargeTime,
            UUIDUtil.STREAM_CODEC,
            ReleaseUsingSpellPayload::uuid,
            ReleaseUsingSpellPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
