package net.stln.magitech.datagen.recipe;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class WoodRecipeGenerator {

    public static void buildWoodRecipes(RecipeOutput output, TagKey<Item> logTag, Item log, Item wood, Item strippedLog, Item strippedWood, Item planks,
                                        Item slab, Item stairs, Item fence, Item fenceGate, Item door, Item trapdoor,
                                        Item pressurePlate, Item button, Item sign, Item hangingSign, Item boat, Item chestBoat) {
        new ShapelessRecipeBuilder(RecipeCategory.BUILDING_BLOCKS, planks, 4)
                .group("planks")
                .requires(logTag)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(logTag))
                .save(output, VanillaSimpleRecipeGenerator.craftingId(planks));
        VanillaSimpleRecipeGenerator.twoByTwo(output, Ingredient.of(log), new ItemStack(wood, 3), RecipeCategory.BUILDING_BLOCKS, "bark");
        VanillaSimpleRecipeGenerator.twoByTwo(output, Ingredient.of(strippedLog), new ItemStack(strippedWood, 3), RecipeCategory.BUILDING_BLOCKS, "bark");
        BlockSetRecipeGenerator.slab(output, planks, slab, RecipeCategory.BUILDING_BLOCKS, "wooden_slab");
        BlockSetRecipeGenerator.stairs(output, planks, stairs, RecipeCategory.BUILDING_BLOCKS, "wooden_stairs");
        BlockSetRecipeGenerator.fence(output, planks, fence, RecipeCategory.MISC, "wooden_fence");
        BlockSetRecipeGenerator.fenceGate(output, planks, fenceGate, RecipeCategory.REDSTONE, "wooden_fence_gate");
        BlockSetRecipeGenerator.door(output, planks, door, RecipeCategory.REDSTONE, "wooden_door");
        BlockSetRecipeGenerator.trapdoor(output, planks, trapdoor, RecipeCategory.REDSTONE, "wooden_trapdoor");
        BlockSetRecipeGenerator.pressurePlate(output, planks, pressurePlate, RecipeCategory.REDSTONE, "wooden_pressure_plate");
        BlockSetRecipeGenerator.button(output, planks, button);
        BlockSetRecipeGenerator.sign(output, planks, sign, RecipeCategory.MISC, "wooden_sign");
        BlockSetRecipeGenerator.hangingSign(output, strippedLog, hangingSign, RecipeCategory.MISC, "hanging_sign");
        BlockSetRecipeGenerator.boat(output, planks, boat);
        VanillaSimpleRecipeGenerator.shapeless(output, List.of(Ingredient.of(Items.CHEST), Ingredient.of(boat)), new ItemStack(chestBoat));
    }

}
