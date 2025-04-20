package net.stln.magitech.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;

public record ReleaseUsingSpellPayload(ItemStack stack, int chargeTime, String uuid) implements CustomPacketPayload {
    public static final ResourceLocation RELEASE_USING_SPELL_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "release_using_spell");
    public static final Type<ReleaseUsingSpellPayload> TYPE = new Type<>(RELEASE_USING_SPELL_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ReleaseUsingSpellPayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            ReleaseUsingSpellPayload::stack,
            ByteBufCodecs.INT,
            ReleaseUsingSpellPayload::chargeTime,
            ByteBufCodecs.STRING_UTF8,
            ReleaseUsingSpellPayload::uuid,
            ReleaseUsingSpellPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
