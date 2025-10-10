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
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.recipe.input.CrucibleRecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record ZardiusCrucibleRecipe(String group, List<Ingredient> ingredients, SizedFluidIngredient fluidIngredient,
                                    Optional<ItemStack> result,
                                    Optional<FluidStack> resultFluid) implements Recipe<CrucibleRecipeInput> {
    public static final MapCodec<ZardiusCrucibleRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(ZardiusCrucibleRecipe::group),
            Ingredient.CODEC_NONEMPTY.listOf(1, Integer.MAX_VALUE).fieldOf("ingredients").forGetter(ZardiusCrucibleRecipe::ingredients),
            SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid_ingredient").forGetter(ZardiusCrucibleRecipe::fluidIngredient),
            ItemStack.STRICT_CODEC.optionalFieldOf("result").forGetter(ZardiusCrucibleRecipe::result),
            FluidStack.CODEC.optionalFieldOf("fluid_result").forGetter(ZardiusCrucibleRecipe::resultFluid)
    ).apply(instance, ZardiusCrucibleRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ZardiusCrucibleRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ZardiusCrucibleRecipe::group,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            ZardiusCrucibleRecipe::ingredients,
            SizedFluidIngredient.STREAM_CODEC,
            ZardiusCrucibleRecipe::fluidIngredient,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs::optional),
            ZardiusCrucibleRecipe::result,
            FluidStack.STREAM_CODEC.apply(ByteBufCodecs::optional),
            ZardiusCrucibleRecipe::resultFluid,
            ZardiusCrucibleRecipe::new
    );

    @Override
    public boolean matches(@NotNull CrucibleRecipeInput input, @NotNull Level level) {
        int[] matchedSlots = RecipeMatcher.findMatches(input.items(), ingredients);
        //noinspection ConstantValue
        if (matchedSlots != null) {
            return fluidIngredient.test(input.fluid());
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CrucibleRecipeInput input, HolderLookup.@NotNull Provider registries) {
        return getResultItem(registries);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result.orElse(ItemStack.EMPTY);
    }

    @Override
    public @NotNull String getGroup() {
        return group;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return BlockInit.ZARDIUS_CRUCIBLE.toStack();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeInit.ZARDIUS_CRUCIBLE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeInit.ZARDIUS_CRUCIBLE_TYPE.get();
    }
}
