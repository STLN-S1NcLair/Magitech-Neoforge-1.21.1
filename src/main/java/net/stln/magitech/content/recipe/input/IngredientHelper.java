package net.stln.magitech.content.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientHelper {

    public static boolean testSizedIngs(List<ItemStack> stacks, List<SizedIngredient> ingredients) {
        List<ItemStack> copyInputs = new ArrayList<>(new ArrayList<>(stacks).stream().filter(stack -> !stack.isEmpty()).toList());
        List<SizedIngredient> copyIngs = new ArrayList<>(ingredients);
        for (SizedIngredient ingredient : ingredients) {
            for (ItemStack stack : copyInputs) {
                if (ingredient.test(stack)) {
                    copyInputs.remove(stack);
                    copyIngs.remove(ingredient);
                    break;
                }
            }
        }
        return copyIngs.isEmpty() && copyInputs.isEmpty();
    }

    public static boolean testStackedSizedIngs(List<ItemStack> stacks, List<SizedIngredient> ingredients) {
        List<ItemStack> nonEmptyStacks = stacks.stream()
                .filter(stack -> !stack.isEmpty())
                .map(ItemStack::copy)
                .toList();

        int stackCount = nonEmptyStacks.size();
        int ingredientCount = ingredients.size();

        int source = 0;
        int firstStackNode = 1;
        int firstIngredientNode = firstStackNode + stackCount;
        int sink = firstIngredientNode + ingredientCount;
        int nodeCount = sink + 1;

        int[][] capacity = new int[nodeCount][nodeCount];
        int totalRequired = 0;

        for (int i = 0; i < stackCount; i++) {
            capacity[source][firstStackNode + i] = nonEmptyStacks.get(i).getCount();
        }

        for (int j = 0; j < ingredientCount; j++) {
            int required = Math.max(0, ingredients.get(j).count());
            capacity[firstIngredientNode + j][sink] = required;
            totalRequired += required;
        }

        for (int i = 0; i < stackCount; i++) {
            ItemStack stack = nonEmptyStacks.get(i);
            int stackNode = firstStackNode + i;
            int stackSize = stack.getCount();

            for (int j = 0; j < ingredientCount; j++) {
                if (ingredients.get(j).ingredient().test(stack)) {
                    capacity[stackNode][firstIngredientNode + j] = stackSize;
                }
            }
        }

        int flow = 0;
        int[] parent = new int[nodeCount];

        while (true) {
            java.util.Arrays.fill(parent, -1);
            parent[source] = source;

            java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>();
            queue.add(source);

            while (!queue.isEmpty() && parent[sink] == -1) {
                int from = queue.poll();
                for (int to = 0; to < nodeCount; to++) {
                    if (parent[to] == -1 && capacity[from][to] > 0) {
                        parent[to] = from;
                        queue.add(to);
                    }
                }
            }

            if (parent[sink] == -1) {
                break;
            }

            int add = Integer.MAX_VALUE;
            int node = sink;
            while (node != source) {
                int prev = parent[node];
                add = Math.min(add, capacity[prev][node]);
                node = prev;
            }

            node = sink;
            while (node != source) {
                int prev = parent[node];
                capacity[prev][node] -= add;
                capacity[node][prev] += add;
                node = prev;
            }

            flow += add;
        }

        return flow == totalRequired;
    }
}
