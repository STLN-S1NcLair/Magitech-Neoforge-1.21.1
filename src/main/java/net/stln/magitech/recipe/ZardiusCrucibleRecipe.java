package net.stln.magitech.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public record ZardiusCrucibleRecipe(String group, List<Ingredient> ingredients, FluidStack inputFluid, Optional<ItemStack> result, Optional<FluidStack> outputFluid) implements Recipe<MultiStackRecipeInput> {

    @Override
    public boolean matches(MultiStackRecipeInput input, @NotNull Level level) {
        var nonEmptyItems = new ArrayList<ItemStack>(input.ingredientCount());
        for (var item : input.stacks())
            if (!item.isEmpty())
                nonEmptyItems.add(item);
        return RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull MultiStackRecipeInput input, HolderLookup.@NotNull Provider registries) {
        return result.orElse(ItemStack.EMPTY).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.copyOf(ingredients);
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result.orElse(ItemStack.EMPTY).copy();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeInit.ZARDIUS_CRUCIBLE_TYPE.get();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeInit.ZARDIUS_CRUCIBLE_SERIALIZER.get();
    }

    public interface Factory<T extends ZardiusCrucibleRecipe> {
        T create(String group, List<Ingredient> ingredients, FluidStack inputFluid, Optional<ItemStack> result, Optional<FluidStack> outputFluid);
    }

    public static class Serializer<T extends ZardiusCrucibleRecipe> implements RecipeSerializer<T> {
        final Factory<T> factory;
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

        protected Serializer(Factory<T> factory) {
            this.factory = factory;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_340781_ -> p_340781_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(ZardiusCrucibleRecipe::group),
                                    Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").forGetter(ZardiusCrucibleRecipe::ingredients),
                                    FluidStack.CODEC.fieldOf("input_fluid").forGetter(ZardiusCrucibleRecipe::inputFluid),
                                    ItemStack.STRICT_CODEC.optionalFieldOf("result").forGetter(ZardiusCrucibleRecipe::result),
                                    FluidStack.CODEC.optionalFieldOf("output_fluid").forGetter(ZardiusCrucibleRecipe::outputFluid)
                            )
                            .apply(p_340781_, factory::create)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    ZardiusCrucibleRecipe::group,
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                    ZardiusCrucibleRecipe::ingredients,
                    FluidStack.STREAM_CODEC,
                    ZardiusCrucibleRecipe::inputFluid,
                    ItemStack.STREAM_CODEC.apply(ByteBufCodecs::optional),
                    ZardiusCrucibleRecipe::result,
                    FluidStack.STREAM_CODEC.apply(ByteBufCodecs::optional),
                    ZardiusCrucibleRecipe::outputFluid,
                    factory::create
            );
        }

        @Override
        public @NotNull MapCodec<T> codec() {
            return this.codec;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return this.streamCodec;
        }
    }
}
