package net.stln.magitech.recipe;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record MultiStackWithFluidRecipeInput(List<ItemStack> stacks) implements RecipeInput {
    @Override
    public ItemStack getItem(int p_346205_) {
        return stacks.get(p_346205_);
    }

    @Override
    public int size() {
        return this.stacks.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public int ingredientCount() {
        int i = 0;
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                i++;
            }
        }
        return i;
    }

    public StackedContents stackedContents() {
        final StackedContents stackedContents = new StackedContents();

        for (ItemStack itemstack : stacks) {
            if (!itemstack.isEmpty()) {
                stackedContents.accountStack(itemstack, 1);
            }
        }
        return stackedContents;
    }
}