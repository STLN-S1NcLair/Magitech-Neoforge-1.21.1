package net.stln.magitech.recipe.input;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record MultiStackRecipeInput(List<ItemStack> stacks) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int index) {
        return stacks.get(index);
    }

    @Override
    public int size() {
        return this.stacks.size();
    }

    @Override
    public boolean isEmpty() {
        return this.stacks.isEmpty();
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
