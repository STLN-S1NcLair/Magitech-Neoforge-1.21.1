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
        slab(output, ing, result, RecipeCategory.MISC, "");
    }

    public static void slab(RecipeOutput output, Item ing, Item result, RecipeCategory category, String group) {
        new ShapedRecipeBuilder(category, new ItemStack(result, 6))
                .group(group)
                .pattern("###")
                .define('#', ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    public static void stairs(RecipeOutput output, Item ing, Item result) {
        stairs(output, ing, result, RecipeCategory.MISC, "");
    }

    public static void stairs(RecipeOutput output, Item ing, Item result, RecipeCategory category, String group) {
        new ShapedRecipeBuilder(category, new ItemStack(result, 4))
                .group(group)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    public static void door(RecipeOutput output, Item ing, Item result) {
        door(output, ing, result, RecipeCategory.MISC, "");
    }

    public static void door(RecipeOutput output, Item ing, Item result, RecipeCategory category, String group) {
        new ShapedRecipeBuilder(category, new ItemStack(result, 3))
                .group(group)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    public static void trapdoor(RecipeOutput output, Item ing, Item result) {
        trapdoor(output, ing, result, RecipeCategory.MISC, "");
    }

    public static void trapdoor(RecipeOutput output, Item ing, Item result, RecipeCategory category, String group) {
        new ShapedRecipeBuilder(category, new ItemStack(result, 2))
                .group(group)
                .pattern("###")
                .pattern("###")
                .define('#', ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    public static void pressurePlate(RecipeOutput output, Item ing, Item result) {
        pressurePlate(output, ing, result, RecipeCategory.MISC, "");
    }

    public static void pressurePlate(RecipeOutput output, Item ing, Item result, RecipeCategory category, String group) {
        new ShapedRecipeBuilder(category, new ItemStack(result))
                .group(group)
                .pattern("##")
                .define('#', ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    public static void button(RecipeOutput output, Item ing, Item result) {
        new net.minecraft.data.recipes.ShapelessRecipeBuilder(RecipeCategory.REDSTONE, result, 1)
                .group("wooden_button")
                .requires(ing)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    // STONE

    public static void wall(RecipeOutput output, Item stone, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result, 6))
                .pattern("###")
                .pattern("###")
                .define('#', stone)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(stone))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    public static void stonecutting(RecipeOutput output, Item stone, Item result, int count) {
        VanillaSimpleRecipeGenerator.stonecutting(output, Ingredient.of(stone), result, count);
    }

    public static void stonecutting(RecipeOutput output, Item stone, Item result, int count, String suffix) {
        VanillaSimpleRecipeGenerator.stonecutting(output, Ingredient.of(stone), result, count, suffix);
    }

    // WOOD

    public static void fence(RecipeOutput output, Item planks, Item result) {
        fence(output, planks, result, RecipeCategory.MISC, "");
    }

    public static void fence(RecipeOutput output, Item planks, Item result, RecipeCategory category, String group) {
        new ShapedRecipeBuilder(category, new ItemStack(result, 3))
                .group(group)
                .pattern("#S#")
                .pattern("#S#")
                .define('#', planks)
                .define('S', Items.STICK)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(planks))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    public static void fenceGate(RecipeOutput output, Item planks, Item result) {
        fenceGate(output, planks, result, RecipeCategory.MISC, "");
    }

    public static void fenceGate(RecipeOutput output, Item planks, Item result, RecipeCategory category, String group) {
        new ShapedRecipeBuilder(category, new ItemStack(result))
                .group(group)
                .pattern("S#S")
                .pattern("S#S")
                .define('#', planks)
                .define('S', Items.STICK)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(planks))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    public static void sign(RecipeOutput output, Item planks, Item result) {
        sign(output, planks, result, RecipeCategory.MISC, "");
    }

    public static void sign(RecipeOutput output, Item planks, Item result, RecipeCategory category, String group) {
        new ShapedRecipeBuilder(category, new ItemStack(result, 3))
                .group(group)
                .pattern("###")
                .pattern("###")
                .pattern(" S ")
                .define('#', planks)
                .define('S', Items.STICK)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(planks))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    public static void hangingSign(RecipeOutput output, Item strippedLog, Item result) {
        hangingSign(output, strippedLog, result, RecipeCategory.MISC, "");
    }

    public static void hangingSign(RecipeOutput output, Item strippedLog, Item result, RecipeCategory category, String group) {
        new ShapedRecipeBuilder(category, new ItemStack(result, 6))
                .group(group)
                .pattern("C C")
                .pattern("###")
                .pattern("###")
                .define('#', strippedLog)
                .define('C', Items.CHAIN)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(strippedLog))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }

    public static void boat(RecipeOutput output, Item planks, Item result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, new ItemStack(result))
                .pattern("# #")
                .pattern("###")
                .define('#', planks)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(planks))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(result));
    }
}
