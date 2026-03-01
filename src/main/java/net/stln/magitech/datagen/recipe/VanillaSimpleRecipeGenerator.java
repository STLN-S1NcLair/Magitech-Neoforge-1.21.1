package net.stln.magitech.datagen.recipe;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class VanillaSimpleRecipeGenerator {

    public static void oneByOne(RecipeOutput output, Ingredient ing, ItemStack result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, result)
                .pattern("#")
                .define('#', ing)
                .unlockedBy("has_input", has(ing))
                .save(output);
    }

    public static void twoByTwo(RecipeOutput output, Ingredient ing, ItemStack result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, result)
                .pattern("##")
                .pattern("##")
                .define('#', ing)
                .unlockedBy("has_input", has(ing))
                .save(output);
    }

    public static void threeByThree(RecipeOutput output, Ingredient ing, ItemStack result) {
        new ShapedRecipeBuilder(RecipeCategory.MISC, result)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ing)
                .unlockedBy("has_input", has(ing))
                .save(output);
    }

    public static void shapeless(RecipeOutput output, List<Ingredient> ing, ItemStack result) {
        var recipe = new ShapelessRecipeBuilder(RecipeCategory.MISC, result);
        for (Ingredient i : ing) {
            recipe.requires(i);
        }
        for (Ingredient i : ing) {
            recipe.unlockedBy("has_input", has(i));
        }
        recipe.save(output);
    }

    public static void stonecutting(RecipeOutput output, Ingredient ing, Item result, int count) {
        SingleItemRecipeBuilder.stonecutting(ing, RecipeCategory.MISC, result, count)
                .unlockedBy("has_input", VanillaSimpleRecipeGenerator.has(ing))
                .save(output, RecipeBuilder.getDefaultRecipeId(result).withSuffix("_" + RecipeBuilder.getDefaultRecipeId(ing.getItems()[0].getItem()).getPath() + "_stonecutting"));
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(Ingredient ing) {
        return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                new InventoryChangeTrigger.TriggerInstance(Optional.empty(),
                        InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(Arrays.stream(ing.getItems()).map(ItemStack::getItem).toList().toArray(new ItemLike[]{})).build())));
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(Item... items) {
        return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                new InventoryChangeTrigger.TriggerInstance(Optional.empty(),
                        InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(items).build())));
    }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> tag) {
        return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                new InventoryChangeTrigger.TriggerInstance(Optional.empty(),
                        InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(ItemPredicate.Builder.item().of(tag).build())));
    }
}
