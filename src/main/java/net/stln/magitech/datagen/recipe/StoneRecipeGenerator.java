package net.stln.magitech.datagen.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class StoneRecipeGenerator {

    public static void buildStoneRecipesWithPolishedAndBrick(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall, Item polished, Item polishedSlab, Item polishedStairs, Item polishedWall, Item brick, Item brickSlab, Item brickStairs, Item brickWall) {
        buildStoneRecipesWithPolished(output, stone, slab, stairs, wall, polished, polishedSlab, polishedStairs, polishedWall);
        buildStoneRecipes(output, brick, brickSlab, brickStairs, brickWall);
        polishingRecipe(output, polished, brick);
        stonecutting(output, stone, brickSlab, brickStairs, brickWall);
    }

    public static void buildStoneRecipesWithPolished(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall, Item polished, Item polishedSlab, Item polishedStairs, Item polishedWall) {
        buildStoneRecipes(output, stone, slab, stairs, wall);
        buildStoneRecipes(output, polished, polishedSlab, polishedStairs, polishedWall);
        polishingRecipe(output, stone, polished);
        stonecutting(output, stone, polishedSlab, polishedStairs, polishedWall);
    }

    public static void buildStoneRecipes(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall) {
        BlockSetRecipeGenerator.slab(output, stone, slab);
        BlockSetRecipeGenerator.stairs(output, stone, stairs);
        BlockSetRecipeGenerator.wall(output, stone, wall);
        stonecutting(output, stone, slab, stairs, wall);
    }

    public static void stonecutting(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall) {
        BlockSetRecipeGenerator.stonecutting(output, stone, slab, 2);
        BlockSetRecipeGenerator.stonecutting(output, stone, stairs, 1);
        BlockSetRecipeGenerator.stonecutting(output, stone, wall, 1);
    }

    public static void polishingRecipe(RecipeOutput output, Item stone, Item polished) {
        VanillaSimpleRecipeGenerator.twoByTwo(output, Ingredient.of(stone), new ItemStack(polished, 4));
        BlockSetRecipeGenerator.stonecutting(output, stone, polished, 1);
    }

    public static void buildStoneRecipesFromResourceBlock(RecipeOutput output, Item stone, Item brick, Item slab, Item stairs, Item wall) {
        brickFromResourceBlock(output, stone, brick);
        stonecuttingFromResourceBlock(output, stone, slab, stairs, wall);
        buildStoneRecipes(output, brick, slab, stairs, wall);
    }

    public static void brickFromResourceBlock(RecipeOutput output, Item stone, Item brick) {
        VanillaSimpleRecipeGenerator.twoByTwo(output, Ingredient.of(stone), new ItemStack(brick, 16));
        BlockSetRecipeGenerator.stonecutting(output, stone, brick, 4);
    }

    public static void stonecuttingFromResourceBlock(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall) {
        BlockSetRecipeGenerator.stonecutting(output, stone, slab, 8);
        BlockSetRecipeGenerator.stonecutting(output, stone, stairs, 4);
        BlockSetRecipeGenerator.stonecutting(output, stone, wall, 4);
    }
}
