package net.stln.magitech.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.gui.PartCuttingScreen;
import net.stln.magitech.gui.ToolAssemblyScreen;
import net.stln.magitech.recipe.PartCuttingRecipe;
import net.stln.magitech.recipe.ToolAssemblyRecipe;
import net.stln.magitech.recipe.RecipeInit;

import java.util.List;

@JeiPlugin
public class MagitechJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new PartCuttingRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ToolAssemblyRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<PartCuttingRecipe> partCuttingRecipes = recipeManager.getAllRecipesFor(RecipeInit.PART_CUTTING_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(PartCuttingRecipeCategory.PART_CUTTING_RECIPE_TYPE, partCuttingRecipes);
        List<ToolAssemblyRecipe> toolAssemblyRecipes = recipeManager.getAllRecipesFor(RecipeInit.TOOL_ASSEMBLY_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(ToolAssemblyRecipeCategory.TOOL_ASSEMBLY_RECIPE_TYPE, toolAssemblyRecipes);
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
    }
}
