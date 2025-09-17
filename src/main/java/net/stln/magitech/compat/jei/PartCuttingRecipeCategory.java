package net.stln.magitech.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.MaterialComponent;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.recipe.PartCuttingRecipe;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ToolMaterialRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PartCuttingRecipeCategory implements IRecipeCategory<PartCuttingRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "part_cutting");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID,
            "textures/gui/jei_widgets.png");

    public static final RecipeType<PartCuttingRecipe> PART_CUTTING_RECIPE_TYPE =
            new RecipeType<>(UID, PartCuttingRecipe.class);

    private final IDrawable icon;

    public PartCuttingRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.ENGINEERING_WORKBENCH));
    }

    @Override
    public RecipeType<PartCuttingRecipe> getRecipeType() {
        return PART_CUTTING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.magitech.part_cutting");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(PartCuttingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
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
    public void setRecipe(IRecipeLayoutBuilder builder, PartCuttingRecipe recipe, IFocusGroup focuses) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<ToolMaterialRecipe> materialRecipes = recipeManager.getAllRecipesFor(RecipeInit.TOOL_MATERIAL_TYPE.get()).stream().map(RecipeHolder::value).toList();
        List<ItemStack> inputs = new ArrayList<>();
        List<ItemStack> results = new ArrayList<>();
        for (ToolMaterialRecipe materialRecipe : materialRecipes) {
            Ingredient ingredient = materialRecipe.getIngredients().get(0);
            for (ItemStack itemStack : ingredient.getItems()) {
                if (itemStack.isEmpty()) continue;
                itemStack.setCount(recipe.getCount());
                inputs.add(itemStack.copy());
            }
            ItemStack resultStack = recipe.getResultItem(null).copy();
            resultStack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(ToolMaterialRegister.getMaterial(materialRecipe.getResultId())));
            results.add(resultStack);
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 5).addItemStacks(inputs);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 66, 5).addItemStacks(results);
    }
}
