package net.stln.magitech.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.ItemTagKeys;
import net.stln.magitech.datagen.recipe.StoneRecipeGenerator;
import net.stln.magitech.datagen.recipe.WoodRecipeGenerator;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        StoneRecipeGenerator.buildStoneRecipesWithPolishedAndBrick(recipeOutput,
                BlockInit.ALCHECRYSITE_ITEM.get(), BlockInit.ALCHECRYSITE_SLAB_ITEM.get(), BlockInit.ALCHECRYSITE_STAIRS_ITEM.get(), BlockInit.ALCHECRYSITE_WALL_ITEM.get(),
                BlockInit.POLISHED_ALCHECRYSITE_ITEM.get(), BlockInit.POLISHED_ALCHECRYSITE_SLAB_ITEM.get(), BlockInit.POLISHED_ALCHECRYSITE_STAIRS_ITEM.get(), BlockInit.POLISHED_ALCHECRYSITE_WALL_ITEM.get(),
                BlockInit.ALCHECRYSITE_BRICKS_ITEM.get(), BlockInit.ALCHECRYSITE_BRICK_SLAB_ITEM.get(), BlockInit.ALCHECRYSITE_BRICK_STAIRS_ITEM.get(), BlockInit.ALCHECRYSITE_BRICK_WALL_ITEM.get()
        );
        StoneRecipeGenerator.buildStoneRecipes(recipeOutput, BlockInit.VESPERITE_ITEM.get(), BlockInit.VESPERITE_SLAB_ITEM.get(), BlockInit.VESPERITE_STAIRS_ITEM.get(), BlockInit.VESPERITE_WALL_ITEM.get());
        StoneRecipeGenerator.buildStoneRecipesFromResourceBlock(recipeOutput, BlockInit.FLUORITE_BLOCK_ITEM.get(), BlockInit.FLUORITE_BRICKS_ITEM.get(),
                BlockInit.FLUORITE_BRICK_SLAB_ITEM.get(), BlockInit.FLUORITE_BRICK_STAIRS_ITEM.get(), BlockInit.FLUORITE_BRICK_WALL_ITEM.get());

        WoodRecipeGenerator.buildWoodRecipes(recipeOutput, ItemTagKeys.CELIFERN_LOGS, BlockInit.CELIFERN_LOG_ITEM.get(), BlockInit.CELIFERN_WOOD_ITEM.get(), BlockInit.STRIPPED_CELIFERN_LOG_ITEM.get(), BlockInit.STRIPPED_CELIFERN_WOOD_ITEM.get(),
                BlockInit.CELIFERN_PLANKS_ITEM.get(), BlockInit.CELIFERN_SLAB_ITEM.get(), BlockInit.CELIFERN_STAIRS_ITEM.get(), BlockInit.CELIFERN_FENCE_ITEM.get(), BlockInit.CELIFERN_FENCE_GATE_ITEM.get(),
                BlockInit.CELIFERN_DOOR_ITEM.get(), BlockInit.CELIFERN_TRAPDOOR_ITEM.get(), BlockInit.CELIFERN_PRESSURE_PLATE_ITEM.get(), BlockInit.CELIFERN_BUTTON_ITEM.get(),
                BlockInit.CELIFERN_SIGN_ITEM.get(), BlockInit.CELIFERN_HANGING_SIGN_ITEM.get(), ItemInit.CELIFERN_BOAT.get(), ItemInit.CELIFERN_CHEST_BOAT.get());
        WoodRecipeGenerator.buildWoodRecipes(recipeOutput, ItemTagKeys.CHARCOAL_BIRCH_LOGS, BlockInit.CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.CHARCOAL_BIRCH_WOOD_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM.get(),
                BlockInit.CHARCOAL_BIRCH_PLANKS_ITEM.get(), BlockInit.CHARCOAL_BIRCH_SLAB_ITEM.get(), BlockInit.CHARCOAL_BIRCH_STAIRS_ITEM.get(), BlockInit.CHARCOAL_BIRCH_FENCE_ITEM.get(), BlockInit.CHARCOAL_BIRCH_FENCE_GATE_ITEM.get(),
                BlockInit.CHARCOAL_BIRCH_DOOR_ITEM.get(), BlockInit.CHARCOAL_BIRCH_TRAPDOOR_ITEM.get(), BlockInit.CHARCOAL_BIRCH_PRESSURE_PLATE_ITEM.get(), BlockInit.CHARCOAL_BIRCH_BUTTON_ITEM.get(),
                BlockInit.CHARCOAL_BIRCH_SIGN_ITEM.get(), BlockInit.CHARCOAL_BIRCH_HANGING_SIGN_ITEM.get(), ItemInit.CHARCOAL_BIRCH_BOAT.get(), ItemInit.CHARCOAL_BIRCH_CHEST_BOAT.get());
        WoodRecipeGenerator.buildWoodRecipes(recipeOutput, ItemTagKeys.MYSTWOOD_LOGS, BlockInit.MYSTWOOD_LOG_ITEM.get(), BlockInit.MYSTWOOD_WOOD_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM.get(),
                BlockInit.MYSTWOOD_PLANKS_ITEM.get(), BlockInit.MYSTWOOD_SLAB_ITEM.get(), BlockInit.MYSTWOOD_STAIRS_ITEM.get(), BlockInit.MYSTWOOD_FENCE_ITEM.get(), BlockInit.MYSTWOOD_FENCE_GATE_ITEM.get(),
                BlockInit.MYSTWOOD_DOOR_ITEM.get(), BlockInit.MYSTWOOD_TRAPDOOR_ITEM.get(), BlockInit.MYSTWOOD_PRESSURE_PLATE_ITEM.get(), BlockInit.MYSTWOOD_BUTTON_ITEM.get(),
                BlockInit.MYSTWOOD_SIGN_ITEM.get(), BlockInit.MYSTWOOD_HANGING_SIGN_ITEM.get(), ItemInit.MYSTWOOD_BOAT.get(), ItemInit.MYSTWOOD_CHEST_BOAT.get());
    }
}
