package net.stln.magitech.content.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CrucibleRecipeInput(List<ItemStack> items, FluidStack fluid) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return (items.isEmpty() || items.stream().allMatch(ItemStack::isEmpty)) && fluid.isEmpty();
    }

    public boolean test(List<SizedIngredient> ingredients, SizedFluidIngredient fluid) {
        if (!fluid.test(this.fluid)) {
            return false;
        }
        return IngredientHelper.testSizedIngs(this.items, ingredients);
    }
}
