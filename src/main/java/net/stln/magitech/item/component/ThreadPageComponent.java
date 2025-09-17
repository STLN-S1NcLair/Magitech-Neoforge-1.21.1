package net.stln.magitech.item.component;


import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.RegistryHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record ThreadPageComponent(@NotNull Holder<Spell> spell) {
    public static final Codec<ThreadPageComponent> CODEC = Spell.HOLDER_CODEC.xmap(ThreadPageComponent::new, ThreadPageComponent::spell);
    public static final StreamCodec<RegistryFriendlyByteBuf, ThreadPageComponent> STREAM_CODEC = Spell.HOLDER_STREAM_CODEC.map(ThreadPageComponent::new, ThreadPageComponent::spell);

    public ThreadPageComponent(DeferredHolder<Spell, ?> spell) {
        this(spell.getDelegate());
    }
    
    public @NotNull Optional<ResourceLocation> getId() {
        return RegistryHelper.getId(spell);
    }
}

