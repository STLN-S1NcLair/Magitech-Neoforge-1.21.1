package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellLike;
import org.jetbrains.annotations.NotNull;

public record ThreadPageComponent(@NotNull Spell spell) {
    public static final Codec<ThreadPageComponent> CODEC = Spell.CODEC.xmap(ThreadPageComponent::new, ThreadPageComponent::spell);
    public static final StreamCodec<RegistryFriendlyByteBuf, ThreadPageComponent> STREAM_CODEC = Spell.STREAM_CODEC.map(ThreadPageComponent::new, ThreadPageComponent::spell);

    public ThreadPageComponent(SpellLike spell) {
        this(spell.asSpell());
    }
}

