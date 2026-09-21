package net.stln.magitech.compat.jei;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluence;

import java.util.List;

public record FieldEffectCompositionJeiRecipe(
        ResourceLocation id,
        FieldEffectType fieldEffect,
        List<FieldInfluence> influences
) {
    public static final Codec<FieldEffectCompositionJeiRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(FieldEffectCompositionJeiRecipe::id),
            FieldEffectType.CODEC.fieldOf("field_effect").forGetter(FieldEffectCompositionJeiRecipe::fieldEffect),
            FieldInfluence.CODEC.listOf().fieldOf("influences").forGetter(FieldEffectCompositionJeiRecipe::influences)
    ).apply(instance, FieldEffectCompositionJeiRecipe::new));

    public FieldEffectCompositionJeiRecipe {
        influences = List.copyOf(influences);
    }
}
