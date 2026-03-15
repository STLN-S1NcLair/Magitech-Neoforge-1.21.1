package net.stln.magitech.content.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.magic.spell.ISpell;

import java.util.Optional;

public record SpellCastPayload(ISpell spell, Optional<ItemStack> wand, int id) implements CustomPacketPayload {
    public static final ResourceLocation ID = Magitech.id("spell_cast");
    public static final Type<SpellCastPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellCastPayload> STREAM_CODEC = StreamCodec.composite(
            ISpell.STREAM_CODEC,
            SpellCastPayload::spell,
            ByteBufCodecs.optional(ItemStack.STREAM_CODEC),
            SpellCastPayload::wand,
            ByteBufCodecs.INT,
            SpellCastPayload::id,
            SpellCastPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
