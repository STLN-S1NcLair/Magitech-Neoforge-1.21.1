package net.stln.magitech.content.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FieldEffectRecipe implements Recipe<SingleRecipeInput> {
    public static final MapCodec<FieldEffectRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter((FieldEffectRecipe recipe) -> recipe.group),
            SizedIngredient.NESTED_CODEC.optionalFieldOf("ingredient").forGetter((FieldEffectRecipe recipe) -> recipe.ingredient),
            SizedFluidIngredient.FLAT_CODEC.optionalFieldOf("fluid_ingredient").forGetter((FieldEffectRecipe recipe) -> recipe.fluidIngredient),
            FieldEffectType.CODEC.fieldOf("field_effect").forGetter((FieldEffectRecipe recipe) -> recipe.fieldEffect),
            ItemStack.STRICT_CODEC.listOf(0, Integer.MAX_VALUE).fieldOf("results").forGetter((FieldEffectRecipe recipe) -> recipe.results),
            FluidStack.CODEC.optionalFieldOf("fluid_result").forGetter((FieldEffectRecipe recipe) -> recipe.fluidResult)
    ).apply(instance, FieldEffectRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FieldEffectRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            recipe -> recipe.group,
            SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs::optional),
            recipe -> recipe.ingredient,
            SizedFluidIngredient.STREAM_CODEC.apply(ByteBufCodecs::optional),
            recipe -> recipe.fluidIngredient,
            ByteBufCodecs.registry(MagitechRegistries.Keys.FIELD_EFFECT_TYPE),
            recipe -> recipe.fieldEffect,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
            recipe -> recipe.results,
            FluidStack.STREAM_CODEC.apply(ByteBufCodecs::optional),
            recipe -> recipe.fluidResult,
            FieldEffectRecipe::new
    );

    protected final String group;
    protected final Optional<SizedIngredient> ingredient;
    protected final Optional<SizedFluidIngredient> fluidIngredient;
    protected final FieldEffectType fieldEffect;
    protected final List<ItemStack> results;
    protected final Optional<FluidStack> fluidResult;

    public FieldEffectRecipe(String group, SizedIngredient ingredient, FieldEffectType fieldEffect, List<ItemStack> results) {
        this(group, Optional.of(ingredient), Optional.empty(), fieldEffect, results, Optional.empty());
    }

    public FieldEffectRecipe(String group, SizedIngredient ingredient, FieldEffectType fieldEffect, FluidStack fluidResult) {
        this(group, Optional.of(ingredient), Optional.empty(), fieldEffect, List.of(), Optional.of(fluidResult));
    }

    public FieldEffectRecipe(String group, SizedFluidIngredient fluidIngredient, FieldEffectType fieldEffect, List<ItemStack> results) {
        this(group, Optional.empty(), Optional.of(fluidIngredient), fieldEffect, results, Optional.empty());
    }

    public FieldEffectRecipe(
            String group,
            Optional<SizedIngredient> ingredient,
            Optional<SizedFluidIngredient> fluidIngredient,
            FieldEffectType fieldEffect,
            List<ItemStack> results,
            Optional<FluidStack> fluidResult
    ) {
        if (ingredient.isEmpty() && fluidIngredient.isEmpty()) {
            throw new IllegalArgumentException("A field-effect recipe must have an item or fluid ingredient");
        }
        this.group = Objects.requireNonNull(group);
        this.ingredient = Objects.requireNonNull(ingredient);
        this.fluidIngredient = Objects.requireNonNull(fluidIngredient);
        this.fieldEffect = Objects.requireNonNull(fieldEffect);
        this.results = results.stream().map(ItemStack::copy).toList();
        this.fluidResult = fluidResult.map(FluidStack::copy);
    }

    @Override
    public boolean matches(@NotNull SingleRecipeInput input, @NotNull Level level) {
        return matchesItem(input.item());
    }

    public boolean matchesItem(ItemStack input) {
        return ingredient.isPresent() && ingredient.get().test(input);
    }

    public boolean matchesFluid(FluidStack input) {
        return fluidIngredient.isPresent() && fluidIngredient.get().test(input);
    }

    public Optional<SizedIngredient> getIngredient() {
        return ingredient;
    }

    public Optional<SizedFluidIngredient> getFluidIngredient() {
        return fluidIngredient;
    }

    public FieldEffectType getFieldEffect() {
        return fieldEffect;
    }

    public List<ItemStack> getResults() {
        return results.stream().map(ItemStack::copy).toList();
    }

    public Optional<FluidStack> getFluidResult() {
        return fluidResult.map(FluidStack::copy);
    }

    public int inputCount() {
        return ingredient.map(SizedIngredient::count).orElse(1);
    }

    public boolean hasItemIngredient() {
        return ingredient.isPresent();
    }

    public boolean hasFluidIngredient() {
        return fluidIngredient.isPresent();
    }

    public static Optional<FieldEffectRecipe> findItemRecipe(Level level, FieldEffectType effect, ItemStack input) {
        if (input.isEmpty()) {
            return Optional.empty();
        }
        return level.getRecipeManager().getAllRecipesFor(RecipeInit.FIELD_EFFECT_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.fieldEffect == effect)
                .filter(FieldEffectRecipe::hasItemIngredient)
                .filter(recipe -> recipe.inputCount() <= input.getCount())
                .filter(recipe -> recipe.matchesItem(input))
                .findFirst();
    }

    public static Optional<FieldEffectRecipe> findBlockRecipe(Level level, BlockPos pos, FieldEffectType effect) {
        if (level.getFluidState(pos).isSource()) {
            FluidStack input = new FluidStack(level.getFluidState(pos).getType(), 1000);
            Optional<FieldEffectRecipe> fluidRecipe = level.getRecipeManager().getAllRecipesFor(RecipeInit.FIELD_EFFECT_TYPE.get()).stream()
                    .map(RecipeHolder::value)
                    .filter(recipe -> recipe.fieldEffect == effect)
                    .filter(FieldEffectRecipe::hasFluidIngredient)
                    .filter(recipe -> recipe.matchesFluid(input))
                    .findFirst();
            if (fluidRecipe.isPresent()) {
                return fluidRecipe;
            }
        }

        ItemStack blockItem = level.getBlockState(pos).getBlock().asItem().getDefaultInstance();
        if (blockItem.isEmpty()) {
            return Optional.empty();
        }
        return findItemRecipe(level, effect, blockItem).filter(recipe -> recipe.inputCount() <= 1);
    }

    public static List<ItemStack> processItem(Level level, FieldEffectType effect, List<ItemStack> inputs) {
        List<ItemStack> results = new ArrayList<>();
        for (ItemStack input : inputs) {
            findItemRecipe(level, effect, input).ifPresent(recipe -> {
                int batchCount = input.getCount() / recipe.inputCount();
                addScaledResults(results, recipe.results, batchCount);

                int remainderCount = input.getCount() % recipe.inputCount();
                if (remainderCount > 0) {
                    results.add(input.copyWithCount(remainderCount));
                }
            });
        }
        return results;
    }

    public static List<ItemStack> processBlock(Level level, BlockPos pos, FieldEffectRecipe recipe) {
        List<ItemStack> results = recipe.getResults();
        boolean replaced = false;

        if (recipe.fluidResult.isPresent() && !recipe.fluidResult.get().isEmpty()) {
            level.setBlock(pos, recipe.fluidResult.get().getFluid().defaultFluidState().createLegacyBlock(), 3);
            replaced = true;
        }

        for (ItemStack result : results) {
            if (!replaced && result.getItem() instanceof BlockItem blockItem) {
                level.setBlock(pos, blockItem.getBlock().defaultBlockState(), 3);
                result.shrink(1);
                replaced = true;
            }
        }

        if (!replaced) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
        return results.stream().filter(stack -> !stack.isEmpty()).toList();
    }

    private static void addScaledResults(List<ItemStack> results, List<ItemStack> recipeResults, int batchCount) {
        if (batchCount <= 0) {
            return;
        }
        for (ItemStack recipeResult : recipeResults) {
            ItemStack result = recipeResult.copy();
            long count = (long) result.getCount() * batchCount;
            result.setCount((int) Math.min(Integer.MAX_VALUE, count));
            if (!result.isEmpty()) {
                results.add(result);
            }
        }
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SingleRecipeInput input, HolderLookup.@NotNull Provider registries) {
        return getResultItem(registries);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).copy();
    }

    @Override
    public @NotNull String getGroup() {
        return group;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeInit.FIELD_EFFECT_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeInit.FIELD_EFFECT_TYPE.get();
    }
}
