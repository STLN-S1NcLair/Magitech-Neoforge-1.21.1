package net.stln.magitech.content.field_effect.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

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

    @Override
    public boolean canProcess(Level level, List<ItemStack> inputs) {
        for (ItemStack stack : combineInputs(inputs)) {
            if (findRecipe(level, stack).isPresent()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canProcess(Level level, BlockPos pos) {
        ItemStack input = level.getBlockState(pos).getBlock().asItem().getDefaultInstance();
        return findRecipe(level, input).isPresent();
    }

    @Override
    public List<ItemStack> processItem(Level level, List<ItemStack> inputs) {
        List<ItemStack> results = new ArrayList<>();
        for (ItemStack stack : combineInputs(inputs)) {
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

    private Optional<RecipeMatch<R>> findRecipe(Level level, ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }

        Optional<RecipeHolder<R>> matchingRecipe = level.getRecipeManager().getRecipeFor(
                getRecipeType(), createRecipeInput(stack), level
        );
        if (matchingRecipe.isEmpty()) {
            return Optional.empty();
        }

        RecipeHolder<R> holder = matchingRecipe.get();
        for (int inputCount = 1; inputCount <= stack.getCount(); inputCount++) {
            ItemStack recipeInput = stack.copy();
            recipeInput.setCount(inputCount);
            Optional<RecipeHolder<R>> candidate = level.getRecipeManager().getRecipeFor(
                    getRecipeType(), createRecipeInput(recipeInput), level
            );
            if (candidate.isPresent() && candidate.get().id().equals(holder.id())) {
                return Optional.of(new RecipeMatch<>(holder, inputCount));
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
