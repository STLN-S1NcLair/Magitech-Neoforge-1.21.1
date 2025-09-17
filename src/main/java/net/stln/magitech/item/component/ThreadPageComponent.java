package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.magic.spell.SpellRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ThreadPageComponent(@NotNull Spell spell) {

    public static final Codec<ThreadPageComponent> CODEC = Spell.CODEC.xmap(ThreadPageComponent::new, ThreadPageComponent::spell);
    public static final StreamCodec<ByteBuf, ThreadPageComponent> STREAM_CODEC = Spell.STREAM_CODEC.map(ThreadPageComponent::new, ThreadPageComponent::spell);

    public @Nullable ResourceLocation getId() {
        return SpellRegister.getId(spell);
    }
}

