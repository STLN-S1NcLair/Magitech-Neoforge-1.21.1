package net.stln.magitech.content.field_effect.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.content.recipe.FieldEffectRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * レシピを参照してアイテムを処理するフィールド効果の基底クラスです。
 * Base class for field effects that process items by referring to recipes.
 *
 * @param <I> レシピ入力の型 / recipe input type
 * @param <R> レシピの型 / recipe type
 */
public abstract class RecipeFieldEffectType<I extends RecipeInput, R extends Recipe<I>> extends DefaultFieldEffectType {

    /**
     * この効果が参照するレシピ種別を返します。
     * Returns the recipe type referenced by this effect.
     */
    protected abstract RecipeType<R> getRecipeType();

    /**
     * アイテムスタックからレシピ入力を作成します。
     * Creates a recipe input from an item stack.
     */
    protected abstract I createRecipeInput(ItemStack stack);

    public final List<RecipeHolder<FieldEffectRecipe>> getRuntimeFieldEffectRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor(getRecipeType()).stream()
                .flatMap(holder -> createFieldEffectRecipe(level, holder)
                        .stream()
                        .map(recipe -> new RecipeHolder<>(runtimeRecipeId(holder.id()), recipe)))
                .toList();
    }

    protected Optional<FieldEffectRecipe> createFieldEffectRecipe(Level level, RecipeHolder<R> holder) {
        R recipe = holder.value();
        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients.isEmpty()) {
            return Optional.empty();
        }

        ItemStack result = recipe.getResultItem(level.registryAccess());
        if (result.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new FieldEffectRecipe(
                "",
                new SizedIngredient(ingredients.getFirst(), 1),
                this,
                List.of(result)
        ));
    }

    private ResourceLocation runtimeRecipeId(ResourceLocation recipeId) {
        ResourceLocation fieldEffectId = MagitechRegistries.FIELD_EFFECT_TYPE.getKey(this);
        String fieldEffectPath = fieldEffectId == null
                ? "unknown"
                : fieldEffectId.getNamespace() + "/" + fieldEffectId.getPath();
        return Magitech.id("field_effect/runtime/" + fieldEffectPath + "/" + recipeId.getNamespace() + "/" + recipeId.getPath());
    }

    @Override
    public boolean canProcess(@NotNull Level level, @NotNull List<ItemStack> inputs) {
        for (ItemStack stack : combineInputs(inputs)) {
            if (findFieldEffectRecipe(level, stack).isPresent() || findRecipe(level, stack).isPresent()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canProcess(@NotNull Level level, @NotNull BlockPos pos) {
        if (findFieldEffectBlockRecipe(level, pos).isPresent()) {
            return true;
        }
        ItemStack input = level.getBlockState(pos).getBlock().asItem().getDefaultInstance();
        return findRecipe(level, input).isPresent();
    }

    @Override
    public @NotNull List<ItemStack> processItem(@NotNull Level level, @NotNull List<ItemStack> inputs) {
        List<ItemStack> results = new ArrayList<>();
        for (ItemStack stack : combineInputs(inputs)) {
            Optional<FieldEffectRecipe> fieldEffectRecipe = findFieldEffectRecipe(level, stack);
            if (fieldEffectRecipe.isPresent()) {
                results.addAll(FieldEffectRecipe.processItem(level, this, List.of(stack)));
                continue;
            }
            findRecipe(level, stack).ifPresent(recipe -> {
                int batchCount = stack.getCount() / recipe.inputCount();
                if (batchCount <= 0) {
                    return;
                }

                ItemStack recipeInput = stack.copy();
                recipeInput.setCount(recipe.inputCount());
                ItemStack result = recipe.holder().value().assemble(
                        createRecipeInput(recipeInput), level.registryAccess()
                );
                long resultCount = (long) result.getCount() * batchCount;
                result.setCount((int) Math.min(Integer.MAX_VALUE, resultCount));
                if (!result.isEmpty()) {
                    results.add(result);
                }

                int remainderCount = stack.getCount() % recipe.inputCount();
                if (remainderCount > 0) {
                    ItemStack remainder = stack.copy();
                    remainder.setCount(remainderCount);
                    results.add(remainder);
                }
            });
        }
        return results;
    }

    @Override
    public @NotNull List<ItemStack> processBlock(@NotNull Level level, @NotNull BlockPos pos) {
        Optional<FieldEffectRecipe> fieldEffectRecipe = findFieldEffectBlockRecipe(level, pos);
        if (fieldEffectRecipe.isPresent()) {
            return processFieldEffectBlock(level, pos, fieldEffectRecipe.get());
        }

        List<ItemStack> results = processItem(level, List.of(level.getBlockState(pos).getBlock().asItem().getDefaultInstance()));
        ItemStack removed = null;
        boolean replaced = false;
        for (ItemStack result : results) {
            if (!replaced && result.getItem() instanceof BlockItem blockItem) {
                replaced = true;
                level.setBlock(pos, blockItem.getBlock().defaultBlockState(), 3);
                result.shrink(1);
                if (result.isEmpty()) {
                    removed = result;
                }
            }
        }
        if (removed != null) {
            results.remove(removed);
        }
        if (!replaced) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
        return results;
    }

    protected Optional<FieldEffectRecipe> findFieldEffectRecipe(Level level, ItemStack stack) {
        return FieldEffectRecipe.findItemRecipe(level, this, stack);
    }

    protected Optional<FieldEffectRecipe> findFieldEffectBlockRecipe(Level level, BlockPos pos) {
        return FieldEffectRecipe.findBlockRecipe(level, pos, this);
    }

    protected List<ItemStack> processFieldEffectBlock(Level level, BlockPos pos, FieldEffectRecipe recipe) {
        return FieldEffectRecipe.processBlock(level, pos, recipe);
    }

    private Optional<RecipeMatch<R>> findRecipe(Level level, ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }

        for (RecipeHolder<R> holder : level.getRecipeManager().getAllRecipesFor(getRecipeType())) {
            for (int inputCount = 1; inputCount <= stack.getCount(); inputCount++) {
                ItemStack recipeInput = stack.copy();
                recipeInput.setCount(inputCount);
                if (holder.value().matches(createRecipeInput(recipeInput), level)) {
                    return Optional.of(new RecipeMatch<>(holder, inputCount));
                }
            }
        }
        return Optional.empty();
    }

    private static List<ItemStack> combineInputs(List<ItemStack> inputs) {
        List<ItemStack> combined = new ArrayList<>();
        for (ItemStack input : inputs) {
            if (input.isEmpty()) {
                continue;
            }

            boolean merged = false;
            for (ItemStack existing : combined) {
                if (ItemStack.isSameItemSameComponents(existing, input)) {
                    existing.grow(input.getCount());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                combined.add(input.copy());
            }
        }
        return combined;
    }

    private record RecipeMatch<T extends Recipe<?>>(RecipeHolder<T> holder, int inputCount) {
    }
}
