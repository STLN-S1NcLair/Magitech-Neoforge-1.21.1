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
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FieldInfluenceSourceRecipeCategory implements IRecipeCategory<FieldInfluenceSourceJeiRecipe> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei/field_influence_source_recipe.png");

    private final IDrawable icon;

    public FieldInfluenceSourceRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, BlockInit.ENVIROMETER.toStack());
    }

    @Override
    public @NotNull RecipeType<FieldInfluenceSourceJeiRecipe> getRecipeType() {
        return RecipeHolderTypeInit.FIELD_INFLUENCE_SOURCE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.field_influence_source");
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public Codec<FieldInfluenceSourceJeiRecipe> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return FieldInfluenceSourceJeiRecipe.CODEC;
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(FieldInfluenceSourceJeiRecipe recipe) {
        return recipe.id();
    }

    @Override
    public void draw(@NotNull FieldInfluenceSourceJeiRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
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
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            @NotNull FieldInfluenceSourceJeiRecipe recipe,
            @NotNull IFocusGroup focuses
    ) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 16).addItemStack(recipe.machine());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 16)
                .addIngredients(FieldInfluenceIngredient.TYPE, java.util.List.of(FieldInfluenceIngredient.of(recipe.influence())));
    }
}
