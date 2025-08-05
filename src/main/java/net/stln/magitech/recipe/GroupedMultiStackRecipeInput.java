package net.stln.magitech.recipe;

import com.ibm.icu.impl.Pair;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.ArrayList;
import java.util.List;

public record GroupedMultiStackRecipeInput(List<List<ItemStack>> stacks) implements RecipeInput {

    // 1次元インデックスでItemStackを取得
    @Override
    public ItemStack getItem(int index) {
        int count = 0;
        for (List<ItemStack> group : stacks) {
            for (ItemStack stack : group) {
                if (count == index) {
                    return stack;
                }
                count++;
            }
        }
        throw new IndexOutOfBoundsException("Index out of bounds for GroupedMultiStackRecipeInput: " + index);
    }

    public List<ItemStack> getItems() {
        List<ItemStack> stacks = new ArrayList<>();
        for (List<ItemStack> group : this.stacks) {
            for (ItemStack stack : group) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    public ItemStack getItem(int index, int innerIndex) {
        try {
            return stacks.get(index).get(innerIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Index out of bounds for GroupedMultiStackRecipeInput: " + index + ", " + innerIndex);
        }
    }

    @Override
    public int size() {
        int total = 0;
        for (List<ItemStack> group : stacks) {
            total += group.size();
        }
        return total;
    }

    public int outerSize() {
        return stacks.size();
    }

    public int innerSize(int index) {
        return stacks.get(index).size();
    }

    @Override
    public boolean isEmpty() {
        for (List<ItemStack> group : stacks) {
            for (ItemStack stack : group) {
                if (!stack.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int ingredientCount() {
        int count = 0;
        for (List<ItemStack> group : stacks) {
            for (ItemStack stack : group) {
                if (!stack.isEmpty()) {
                    count++;
                }
            }
        }
        return count;
    }

    public StackedContents stackedContents() {
        final StackedContents contents = new StackedContents();
        for (List<ItemStack> group : stacks) {
            for (ItemStack stack : group) {
                if (!stack.isEmpty()) {
                    contents.accountStack(stack, 1);
                }
            }
        }
        return contents;
    }

    // オプション：元のグループとインデックスを返すユーティリティ
    public Pair<Integer, Integer> to2DIndex(int flatIndex) {
        int count = 0;
        for (int i = 0; i < stacks.size(); i++) {
            List<ItemStack> group = stacks.get(i);
            for (int j = 0; j < group.size(); j++) {
                if (count == flatIndex) {
                    return Pair.of(i, j);
                }
                count++;
            }
        }
        throw new IndexOutOfBoundsException("Index out of bounds: " + flatIndex);
    }
}