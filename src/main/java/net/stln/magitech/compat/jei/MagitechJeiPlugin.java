package net.stln.magitech.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.gui.PartCuttingScreen;
import net.stln.magitech.gui.ToolAssemblyScreen;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.util.ClientHelper;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class MagitechJeiPlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_UID = Magitech.id("jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new PartCuttingRecipeCategory(guiHelper),
                new ToolAssemblyRecipeCategory(guiHelper),
                new SpellConversionRecipeCategory(guiHelper),
                new ZardiusCrucibleRecipeCategory(guiHelper),
                new AthanorPillarInfusionRecipeCategory(guiHelper),
                new InfuserInfusionRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registration.addRecipes(PartCuttingRecipeCategory.PART_CUTTING_RECIPE_TYPE, ClientHelper.getAllRecipes(RecipeInit.PART_CUTTING_TYPE));
        registration.addRecipes(ToolAssemblyRecipeCategory.TOOL_ASSEMBLY_RECIPE_TYPE, ClientHelper.getAllRecipes(RecipeInit.TOOL_ASSEMBLY_TYPE));
        registration.addRecipes(SpellConversionRecipeCategory.SPELL_CONVERSION_RECIPE_TYPE, ClientHelper.getAllRecipes(RecipeInit.SPELL_CONVERSION_TYPE));
        registration.addRecipes(ZardiusCrucibleRecipeCategory.ZARDIUS_CRUCIBLE_RECIPE_TYPE, ClientHelper.getAllRecipes(RecipeInit.ZARDIUS_CRUCIBLE_TYPE));
        registration.addRecipes(AthanorPillarInfusionRecipeCategory.ATHANOR_PILLAR_INFUSION_RECIPE_TYPE, ClientHelper.getAllRecipes(RecipeInit.ATHANOR_PILLAR_INFUSION_TYPE));
        registration.addRecipes(InfuserInfusionRecipeCategory.INFUSER_INFUSION_RECIPE_TYPE, ClientHelper.getAllRecipes(RecipeInit.INFUSER_INFUSION_TYPE));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(PartCuttingScreen.class, 0, 0, 176, 16, PartCuttingRecipeCategory.PART_CUTTING_RECIPE_TYPE);
        registration.addRecipeClickArea(ToolAssemblyScreen.class, 0, 0, 176, 16, ToolAssemblyRecipeCategory.TOOL_ASSEMBLY_RECIPE_TYPE);
        registration.addRecipeClickArea(ToolAssemblyScreen.class, 73, 39, 56, 36, ToolAssemblyRecipeCategory.TOOL_ASSEMBLY_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(BlockInit.ENGINEERING_WORKBENCH.toStack(), PartCuttingRecipeCategory.PART_CUTTING_RECIPE_TYPE);
        registration.addRecipeCatalyst(BlockInit.ASSEMBLY_WORKBENCH.toStack(), ToolAssemblyRecipeCategory.TOOL_ASSEMBLY_RECIPE_TYPE);
        registration.addRecipeCatalyst(ItemInit.WAND.toStack(), SpellConversionRecipeCategory.SPELL_CONVERSION_RECIPE_TYPE);
        registration.addRecipeCatalyst(BlockInit.ZARDIUS_CRUCIBLE_ITEM.toStack(), ZardiusCrucibleRecipeCategory.ZARDIUS_CRUCIBLE_RECIPE_TYPE);
        registration.addRecipeCatalyst(BlockInit.ATHANOR_PILLAR_ITEM.toStack(), AthanorPillarInfusionRecipeCategory.ATHANOR_PILLAR_INFUSION_RECIPE_TYPE);
        registration.addRecipeCatalyst(BlockInit.INFUSER_ITEM.toStack(), InfuserInfusionRecipeCategory.INFUSER_INFUSION_RECIPE_TYPE);
    }
}
