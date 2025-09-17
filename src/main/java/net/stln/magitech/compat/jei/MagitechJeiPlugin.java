package net.stln.magitech.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.gui.PartCuttingScreen;
import net.stln.magitech.gui.ToolAssemblyScreen;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.recipe.*;

import java.util.List;

@JeiPlugin
public class MagitechJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return Magitech.id("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new PartCuttingRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ToolAssemblyRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SpellConversionRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ZardiusCrucibleRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AthanorPillarInfusionRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = JeiHelper.getRecipeManager();
        if (recipeManager == null) return;

        List<PartCuttingRecipe> partCuttingRecipes = recipeManager.getAllRecipesFor(RecipeInit.PART_CUTTING_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(PartCuttingRecipeCategory.PART_CUTTING_RECIPE_TYPE, partCuttingRecipes);
        List<ToolAssemblyRecipe> toolAssemblyRecipes = recipeManager.getAllRecipesFor(RecipeInit.TOOL_ASSEMBLY_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(ToolAssemblyRecipeCategory.TOOL_ASSEMBLY_RECIPE_TYPE, toolAssemblyRecipes);
        List<SpellConversionRecipe> spellConversionRecipes = recipeManager.getAllRecipesFor(RecipeInit.SPELL_CONVERSION_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(SpellConversionRecipeCategory.SPELL_CONVERSION_RECIPE_TYPE, spellConversionRecipes);
        List<ZardiusCrucibleRecipe> zardiusCrucibleRecipes = recipeManager.getAllRecipesFor(RecipeInit.ZARDIUS_CRUCIBLE_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(ZardiusCrucibleRecipeCategory.ZARDIUS_CRUCIBLE_RECIPE_TYPE, zardiusCrucibleRecipes);
        List<AthanorPillarInfusionRecipe> athanorPillarInfusionRecipes = recipeManager.getAllRecipesFor(RecipeInit.ATHANOR_PILLAR_INFUSION_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(AthanorPillarInfusionRecipeCategory.ATHANOR_PILLAR_INFUSION_RECIPE_TYPE, athanorPillarInfusionRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(PartCuttingScreen.class, 0, 0, 176, 16,
                PartCuttingRecipeCategory.PART_CUTTING_RECIPE_TYPE);
        registration.addRecipeClickArea(ToolAssemblyScreen.class, 0, 0, 176, 16,
                ToolAssemblyRecipeCategory.TOOL_ASSEMBLY_RECIPE_TYPE);
        registration.addRecipeClickArea(ToolAssemblyScreen.class, 73, 39, 56, 36,
                ToolAssemblyRecipeCategory.TOOL_ASSEMBLY_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockInit.ENGINEERING_WORKBENCH.get().asItem()),
                PartCuttingRecipeCategory.PART_CUTTING_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.ASSEMBLY_WORKBENCH.get().asItem()),
                ToolAssemblyRecipeCategory.TOOL_ASSEMBLY_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ItemInit.WAND.get()),
                SpellConversionRecipeCategory.SPELL_CONVERSION_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.ZARDIUS_CRUCIBLE_ITEM.get()),
                ZardiusCrucibleRecipeCategory.ZARDIUS_CRUCIBLE_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.ATHANOR_PILLAR_ITEM.get()),
                AthanorPillarInfusionRecipeCategory.ATHANOR_PILLAR_INFUSION_RECIPE_TYPE);
    }
}
