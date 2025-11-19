package net.stln.magitech.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.recipe.input.SpellRecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record SpellConversionRecipe(String group, Ingredient ingredient, Spell spell,
                                    ItemStack result) implements Recipe<SpellRecipeInput> {
    public static final MapCodec<SpellConversionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(SpellConversionRecipe::group),
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(SpellConversionRecipe::ingredient),
            Spell.CODEC.fieldOf("spell").forGetter(SpellConversionRecipe::spell),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SpellConversionRecipe::result)
    ).apply(instance, SpellConversionRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellConversionRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SpellConversionRecipe::group,
            Ingredient.CONTENTS_STREAM_CODEC,
            SpellConversionRecipe::ingredient,
            Spell.STREAM_CODEC,
            SpellConversionRecipe::spell,
            ItemStack.STREAM_CODEC,
            SpellConversionRecipe::result,
            SpellConversionRecipe::new
    );

    @Override
    public boolean matches(@NotNull SpellRecipeInput input, @NotNull Level level) {
        return ingredient.test(input.item()) && Objects.equals(this.spell, input.spell());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SpellRecipeInput input, HolderLookup.@NotNull Provider registries) {
        return getResultItem(registries);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result().copy();
    }

    @Override
    public @NotNull String getGroup() {
        return group;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return ItemInit.FLUORITE.toStack();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeInit.SPELL_CONVERSION_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeInit.SPELL_CONVERSION_TYPE.get();
    }
}
