package net.stln.magitech.compat.jei;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.core.api.field_effect.FieldInfluence;

public record FieldInfluenceSourceJeiRecipe(
        ResourceLocation id,
        ItemStack machine,
        FieldInfluence influence
) {
    public static final Codec<FieldInfluenceSourceJeiRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(FieldInfluenceSourceJeiRecipe::id),
            ItemStack.STRICT_CODEC.fieldOf("machine").forGetter(FieldInfluenceSourceJeiRecipe::machine),
            FieldInfluence.CODEC.fieldOf("influence").forGetter(FieldInfluenceSourceJeiRecipe::influence)
    ).apply(instance, FieldInfluenceSourceJeiRecipe::new));

    public FieldInfluenceSourceJeiRecipe {
        machine = machine.copy();
    }
}
