package net.stln.magitech.content.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.magic.spell.ISpell;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record SpellEndPayload(ISpell spell, Optional<ItemStack> wand, int id) implements CustomPacketPayload {
    public static final ResourceLocation ID = Magitech.id("spell_end");
    public static final Type<SpellEndPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellEndPayload> STREAM_CODEC = StreamCodec.composite(
            ISpell.STREAM_CODEC,
            SpellEndPayload::spell,
            ByteBufCodecs.optional(ItemStack.STREAM_CODEC),
            SpellEndPayload::wand,
            ByteBufCodecs.INT,
            SpellEndPayload::id,
            SpellEndPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
