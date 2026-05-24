package net.stln.magitech.compat.jei;

import com.mojang.serialization.Codec;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.content.item.component.MaterialComponent;
import net.stln.magitech.content.recipe.CrushingRecipe;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.content.recipe.ToolMaterialRecipe;
import net.stln.magitech.feature.tool.material.MaterialInit;
import net.stln.magitech.helper.ClientHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CrushingRecipeCategory extends AbstractMagitechRecipeCategory<RecipeHolder<CrushingRecipe>> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei/crushing_recipe.png");

    public CrushingRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public CrushingRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.CRUSHER)));
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<CrushingRecipe>> getRecipeType() {
        return RecipeHolderTypeInit.CRUSHING_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.crushing");
    }

    @Override
    public Codec<RecipeHolder<CrushingRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(RecipeHolder<CrushingRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public void draw(@NotNull RecipeHolder<CrushingRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 112, 50);
    }

    @Override
    public int getWidth() {
        return 112;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<CrushingRecipe> recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {
        CrushingRecipe value = recipe.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 16).addItemStacks(List.of(value.getSizedIngredient().getItems()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 16).addItemStack(value.getResultItem(access));
    }
}
