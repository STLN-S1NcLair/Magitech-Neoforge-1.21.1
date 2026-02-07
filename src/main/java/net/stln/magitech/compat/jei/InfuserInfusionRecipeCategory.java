package net.stln.magitech.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.element.Element;
import net.stln.magitech.recipe.InfuserInfusionRecipe;
import net.stln.magitech.util.EnergyFormatter;
import net.stln.magitech.util.RenderHelper;
import org.jetbrains.annotations.NotNull;

public class InfuserInfusionRecipeCategory extends AbstractMagitechRecipeCategory<InfuserInfusionRecipe> {
    public static final ResourceLocation UID = Magitech.id("infuser");
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei_widgets.png");

    public static final RecipeType<InfuserInfusionRecipe> INFUSER_INFUSION_RECIPE_TYPE = new RecipeType<>(UID, InfuserInfusionRecipe.class);

    public InfuserInfusionRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public InfuserInfusionRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.INFUSER)));
    }

    @Override
    public @NotNull RecipeType<InfuserInfusionRecipe> getRecipeType() {
        return INFUSER_INFUSION_RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.infuser_infusion");
    }

    @Override
    public void draw(@NotNull InfuserInfusionRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.blit(TEXTURE, 26, 4, 0, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 48, 8, 0, 18, 21, 10);
        guiGraphics.blit(TEXTURE, 73, 4, 36, 0, 18, 18);

        RenderHelper.renderFramedText(guiGraphics, Minecraft.getInstance().font, Component.translatable("recipe.magitech.required_mana").append(": " + EnergyFormatter.formatValue(recipe.getMana())).getString(), 0, getHeight() - 8, Element.NONE);
    }

    @Override
    public int getWidth() {
        return 117;
    }

    @Override
    public int getHeight() {
        return 34;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull InfuserInfusionRecipe recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {
        Ingredient input = recipe.getIngredients().getFirst();
        ItemStack result = recipe.getResultItem(access);
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 5).addIngredients(input);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 5).addItemStack(result);
    }
}
