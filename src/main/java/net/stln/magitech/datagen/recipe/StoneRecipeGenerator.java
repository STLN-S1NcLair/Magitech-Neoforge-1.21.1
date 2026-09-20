package net.stln.magitech.datagen.recipe;

import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class StoneRecipeGenerator {

    public static void buildStoneRecipesWithPolishedAndBrick(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall, Item polished, Item polishedSlab, Item polishedStairs, Item polishedWall, Item brick, Item brickSlab, Item brickStairs, Item brickWall) {
        buildStoneRecipes(output, stone, slab, stairs, wall, false);
        buildStoneRecipes(output, polished, polishedSlab, polishedStairs, polishedWall, true);
        polishingRecipe(output, stone, polished, "_stonecutting");
        VanillaSimpleRecipeGenerator.twoByTwo(output, Ingredient.of(polished), new ItemStack(brick, 4));
        buildStoneRecipes(output, brick, brickSlab, brickStairs, brickWall, true);
        stonecutting(output, stone, brick, 4, "_from_" + RecipeBuilder.getDefaultRecipeId(stone).getPath() + "_stonecutting");
        stonecutting(output, polished, brick, 4, "_from_" + RecipeBuilder.getDefaultRecipeId(polished).getPath() + "_stonecutting");
        stonecutting(output, stone, brickSlab, 2, "_from_" + RecipeBuilder.getDefaultRecipeId(stone).getPath() + "_stonecutting");
        stonecutting(output, polished, brickSlab, 2, "_from_" + RecipeBuilder.getDefaultRecipeId(polished).getPath() + "_stonecutting");
        stonecutting(output, stone, brickStairs, 1, "_from_" + RecipeBuilder.getDefaultRecipeId(stone).getPath() + "_stonecutting");
        stonecutting(output, polished, brickStairs, 1, "_from_" + RecipeBuilder.getDefaultRecipeId(polished).getPath() + "_stonecutting");
        stonecutting(output, stone, brickWall, 1, "_from_" + RecipeBuilder.getDefaultRecipeId(stone).getPath() + "_stonecutting");
        stonecutting(output, stone, polishedSlab, 2, "_from_" + RecipeBuilder.getDefaultRecipeId(stone).getPath() + "_stonecutting");
        stonecutting(output, stone, polishedStairs, 1, "_from_" + RecipeBuilder.getDefaultRecipeId(stone).getPath() + "_stonecutting");
        stonecutting(output, stone, polishedWall, 1, "_from_" + RecipeBuilder.getDefaultRecipeId(stone).getPath() + "_stonecutting");
        stonecutting(output, polished, brickWall, 1, "_from_" + RecipeBuilder.getDefaultRecipeId(polished).getPath() + "_stonecutting");
    }

    public static void buildStoneRecipesWithPolished(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall, Item polished, Item polishedSlab, Item polishedStairs, Item polishedWall) {
        buildStoneRecipes(output, stone, slab, stairs, wall, false);
        buildStoneRecipes(output, polished, polishedSlab, polishedStairs, polishedWall, true);
        polishingRecipe(output, stone, polished, "_stonecutting");
        stonecutting(output, stone, polishedSlab, polishedStairs, polishedWall);
    }

    public static void buildStoneRecipes(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall) {
        buildStoneRecipes(output, stone, slab, stairs, wall, false);
    }

    private static void buildStoneRecipes(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall, boolean fromInput) {
        BlockSetRecipeGenerator.slab(output, stone, slab);
        BlockSetRecipeGenerator.stairs(output, stone, stairs);
        BlockSetRecipeGenerator.wall(output, stone, wall);
        String inputPath = RecipeBuilder.getDefaultRecipeId(stone).getPath();
        String suffix = fromInput ? "_from_" + inputPath + "_stonecutting" : "_stonecutting";
        stonecutting(output, stone, slab, 2, suffix);
        stonecutting(output, stone, stairs, 1, suffix);
        stonecutting(output, stone, wall, 1, suffix);
    }

    public static void stonecutting(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall) {
        String inputPath = RecipeBuilder.getDefaultRecipeId(stone).getPath();
        stonecutting(output, stone, slab, 2, "_from_" + inputPath + "_stonecutting");
        stonecutting(output, stone, stairs, 1, "_from_" + inputPath + "_stonecutting");
        stonecutting(output, stone, wall, 1, "_from_" + inputPath + "_stonecutting");
    }

    public static void polishingRecipe(RecipeOutput output, Item stone, Item polished) {
        polishingRecipe(output, stone, polished, "_stonecutting");
    }

    private static void polishingRecipe(RecipeOutput output, Item stone, Item polished, String suffix) {
        VanillaSimpleRecipeGenerator.twoByTwo(output, Ingredient.of(stone), new ItemStack(polished, 4));
        stonecutting(output, stone, polished, 1, suffix);
    }

    public static void buildStoneRecipesFromResourceBlock(RecipeOutput output, Item stone, Item brick, Item slab, Item stairs, Item wall) {
        brickFromResourceBlock(output, stone, brick);
        stonecuttingFromResourceBlock(output, stone, slab, stairs, wall);
        buildStoneRecipes(output, brick, slab, stairs, wall, true);
    }

    public static void brickFromResourceBlock(RecipeOutput output, Item stone, Item brick) {
        VanillaSimpleRecipeGenerator.twoByTwo(output, Ingredient.of(stone), new ItemStack(brick, 16));
        stonecutting(output, stone, brick, 4, "_stonecutting");
    }

    public static void stonecuttingFromResourceBlock(RecipeOutput output, Item stone, Item slab, Item stairs, Item wall) {
        String inputPath = RecipeBuilder.getDefaultRecipeId(stone).getPath();
        stonecutting(output, stone, slab, 8, "_from_" + inputPath + "_stonecutting");
        stonecutting(output, stone, stairs, 4, "_from_" + inputPath + "_stonecutting");
        stonecutting(output, stone, wall, 4, "_from_" + inputPath + "_stonecutting");
    }

    private static void stonecutting(RecipeOutput output, Item stone, Item result, int count, String suffix) {
        BlockSetRecipeGenerator.stonecutting(output, stone, result, count, suffix);
    }
}
