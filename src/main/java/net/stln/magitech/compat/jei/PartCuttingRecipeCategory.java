package net.stln.magitech.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.MaterialComponent;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.recipe.PartCuttingRecipe;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ToolMaterialRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PartCuttingRecipeCategory extends AbstractMagitechRecipeCategory<PartCuttingRecipe> {
    public static final ResourceLocation UID = Magitech.id("part_cutting");
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei_widgets.png");

    public static final RecipeType<PartCuttingRecipe> PART_CUTTING_RECIPE_TYPE = new RecipeType<>(UID, PartCuttingRecipe.class);

    public PartCuttingRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public PartCuttingRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.ENGINEERING_WORKBENCH)));
    }

    @Override
    public @NotNull RecipeType<PartCuttingRecipe> getRecipeType() {
        return PART_CUTTING_RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.part_cutting");
    }

    @Override
    public void draw(@NotNull PartCuttingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.blit(TEXTURE, 18, 4, 0, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 40, 8, 0, 18, 21, 10);
        guiGraphics.blit(TEXTURE, 65, 4, 36, 0, 18, 18);
    }

    @Override
    public int getWidth() {
        return 101;
    }

    @Override
    public int getHeight() {
        return 26;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull PartCuttingRecipe recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {
        List<ToolMaterialRecipe> materialRecipes = JeiHelper.getAllRecipes(RecipeInit.TOOL_MATERIAL_TYPE);
        List<ItemStack> inputs = new ArrayList<>();
        List<ItemStack> results = new ArrayList<>();
        for (ToolMaterialRecipe materialRecipe : materialRecipes) {
            Ingredient ingredient = materialRecipe.getIngredients().getFirst();
            for (ItemStack itemStack : ingredient.getItems()) {
                if (itemStack.isEmpty()) continue;
                inputs.add(itemStack.copyWithCount(recipe.getCount()));
            }
            ItemStack resultStack = recipe.getResultItem(access).copy();
            resultStack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(ToolMaterialRegister.getMaterial(materialRecipe.getResultId())));
            results.add(resultStack);
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 5).addItemStacks(inputs);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 66, 5).addItemStacks(results);
    }
}
