package net.stln.magitech.datagen.recipe;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class BlockSetRecipeGenerator {

    // GENERAL

    public static void slab(RecipeOutput output, Item ing, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, 6))
                .pattern("###")
                .define('#', ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output);
    }

    public static void stairs(RecipeOutput output, Item ing, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, 4))
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output);
    }

    public static void door(RecipeOutput output, Item ing, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, 3))
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output);
    }

    public static void trapdoor(RecipeOutput output, Item ing, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, 2))
                .pattern("###")
                .pattern("###")
                .define('#', ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output);
    }

    public static void pressurePlate(RecipeOutput output, Item ing, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result))
                .pattern("##")
                .define('#', ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output);
    }

    public static void button(RecipeOutput output, Item ing, Item result) {
        VanillaSimpleRecipeGenerator.oneByOne(output, Ingredient.of(ing), new ItemStack(result));
    }

    // STONE

    public static void wall(RecipeOutput output, Item stone, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, 6))
                .pattern("###")
                .pattern("###")
                .define('#', stone)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(stone))
                .save(output);
    }

    public static void stonecutting(RecipeOutput output, Item stone, Item result, int count) {
        VanillaSimpleRecipeGenerator.stonecutting(output, Ingredient.of(stone), result, count);
    }

    // WOOD

    public static void fence(RecipeOutput output, Item planks, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, 3))
                .pattern("#S#")
                .pattern("#S#")
                .define('#', planks)
                .define('S', Items.STICK)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(planks))
                .save(output);
    }

    public static void fenceGate(RecipeOutput output, Item planks, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result))
                .pattern("S#S")
                .pattern("S#S")
                .define('#', planks)
                .define('S', Items.STICK)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(planks))
                .save(output);
    }

    public static void sign(RecipeOutput output, Item planks, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, 3))
                .pattern("###")
                .pattern("###")
                .pattern(" S ")
                .define('#', planks)
                .define('S', Items.STICK)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(planks))
                .save(output);
    }

    public static void hangingSign(RecipeOutput output, Item strippedLog, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, 6))
                .pattern("C C")
                .pattern("###")
                .pattern("###")
                .define('#', strippedLog)
                .define('C', Items.CHAIN)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(strippedLog))
                .save(output);
    }

    public static void boat(RecipeOutput output, Item planks, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result))
                .pattern("# #")
                .pattern("###")
                .define('#', planks)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(planks))
                .save(output);
    }
}
