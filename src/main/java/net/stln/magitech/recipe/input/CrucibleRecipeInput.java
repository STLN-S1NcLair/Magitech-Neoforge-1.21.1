package net.stln.magitech.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
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
}
