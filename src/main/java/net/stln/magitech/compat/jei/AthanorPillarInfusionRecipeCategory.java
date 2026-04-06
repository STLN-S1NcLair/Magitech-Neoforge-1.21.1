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
import net.minecraft.client.Minecraft;
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
import net.stln.magitech.content.recipe.AthanorPillarInfusionRecipe;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.content.recipe.ToolMaterialRecipe;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.helper.ClientHelper;
import net.stln.magitech.helper.EnergyFormatter;
import net.stln.magitech.helper.RenderHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class AthanorPillarInfusionRecipeCategory extends AbstractMagitechRecipeCategory<RecipeHolder<AthanorPillarInfusionRecipe>> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei_widgets.png");

    public AthanorPillarInfusionRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public AthanorPillarInfusionRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.ATHANOR_PILLAR)));
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<AthanorPillarInfusionRecipe>> getRecipeType() {
        return RecipeHolderTypeInit.ATHANOR_PILLAR_INFUSION_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.athanor_pillar_infusion");
    }

    @Override
    public Codec<RecipeHolder<AthanorPillarInfusionRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(RecipeHolder<AthanorPillarInfusionRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public void draw(@NotNull RecipeHolder<AthanorPillarInfusionRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        guiGraphics.blit(TEXTURE, 54, 54, 0, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 36, 36, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 72, 72, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 72, 36, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 36, 72, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 54, 18, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 54, 90, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 18, 54, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 90, 54, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 0, 0, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 108, 108, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 108, 0, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 0, 108, 18, 0, 18, 18);

        guiGraphics.blit(TEXTURE, 130, 58, 0, 18, 21, 10);

        guiGraphics.blit(TEXTURE, 155, 54, 36, 0, 18, 18);

        RenderHelper.renderFramedText(guiGraphics, Minecraft.getInstance().font, Component.translatable("recipe.magitech.required_mana").append(": " + EnergyFormatter.formatValue(recipe.value().getMana() / 5) + " x 5").getString(), 0, getHeight() - 8, Element.NONE);
    }

    @Override
    public int getWidth() {
        return 173;
    }

    @Override
    public int getHeight() {
        return 136;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<AthanorPillarInfusionRecipe> recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {

        List<Ingredient> ingredients = recipe.value().getIngredients();
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 55).addItemStack(recipe.value().getBase());

        builder.addSlot(RecipeIngredientRole.INPUT, 37, 37).addIngredients(getOrDefault(ingredients, 0));
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 73).addIngredients(getOrDefault(ingredients, 1));
        builder.addSlot(RecipeIngredientRole.INPUT, 73, 37).addIngredients(getOrDefault(ingredients, 2));
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 73).addIngredients(getOrDefault(ingredients, 3));
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 19).addIngredients(getOrDefault(ingredients, 4));
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 91).addIngredients(getOrDefault(ingredients, 5));
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 55).addIngredients(getOrDefault(ingredients, 6));
        builder.addSlot(RecipeIngredientRole.INPUT, 91, 55).addIngredients(getOrDefault(ingredients, 7));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(getOrDefault(ingredients, 8));
        builder.addSlot(RecipeIngredientRole.INPUT, 109, 109).addIngredients(getOrDefault(ingredients, 9));
        builder.addSlot(RecipeIngredientRole.INPUT, 109, 1).addIngredients(getOrDefault(ingredients, 10));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 109).addIngredients(getOrDefault(ingredients, 11));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 156, 55).addItemStack(recipe.value().getResultItem(access));
    }

    private Ingredient getOrDefault(List<Ingredient> ingredients, int index) {
        if (index < ingredients.size()) {
            return ingredients.get(index);
        } else {
            return Ingredient.EMPTY;
        }
    }
}
