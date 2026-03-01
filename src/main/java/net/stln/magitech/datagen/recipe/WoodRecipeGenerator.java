package net.stln.magitech.datagen.recipe;

import net.minecraft.data.recipes.RecipeOutput;
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
        VanillaSimpleRecipeGenerator.twoByTwo(output, Ingredient.of(logTag), new ItemStack(planks, 4));
        VanillaSimpleRecipeGenerator.twoByTwo(output, Ingredient.of(log), new ItemStack(wood, 3));
        VanillaSimpleRecipeGenerator.twoByTwo(output, Ingredient.of(strippedLog), new ItemStack(strippedWood, 3));
        BlockSetRecipeGenerator.slab(output, planks, slab);
        BlockSetRecipeGenerator.stairs(output, planks, stairs);
        BlockSetRecipeGenerator.fence(output, planks, fence);
        BlockSetRecipeGenerator.fenceGate(output, planks, fenceGate);
        BlockSetRecipeGenerator.door(output, planks, door);
        BlockSetRecipeGenerator.trapdoor(output, planks, trapdoor);
        BlockSetRecipeGenerator.pressurePlate(output, planks, pressurePlate);
        BlockSetRecipeGenerator.button(output, planks, button);
        BlockSetRecipeGenerator.sign(output, planks, sign);
        BlockSetRecipeGenerator.hangingSign(output, strippedLog, hangingSign);
        BlockSetRecipeGenerator.boat(output, planks, boat);
        VanillaSimpleRecipeGenerator.shapeless(output, List.of(Ingredient.of(Items.CHEST), Ingredient.of(boat)), new ItemStack(chestBoat));
    }

}
