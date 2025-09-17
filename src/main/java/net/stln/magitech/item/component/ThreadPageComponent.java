package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.magic.spell.Spell;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record ThreadPageComponent(@NotNull Spell spell) {

    public static final Codec<ThreadPageComponent> CODEC = Spell.CODEC.xmap(ThreadPageComponent::new, ThreadPageComponent::spell);
    public static final StreamCodec<RegistryFriendlyByteBuf, ThreadPageComponent> STREAM_CODEC = Spell.STREAM_CODEC.map(ThreadPageComponent::new, ThreadPageComponent::spell);

    public ThreadPageComponent(@NotNull Supplier<? extends Spell> supplier) {
        this(supplier.get());
    }
    
    public @Nullable ResourceLocation getId() {
        return MagitechRegistries.SPELL.getKeyOrNull(spell);
    }
}

