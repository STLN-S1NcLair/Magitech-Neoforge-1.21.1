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
import net.stln.magitech.content.recipe.InfusionRecipe;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.EnergyFormatter;
import net.stln.magitech.helper.RenderHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class InfusionRecipeCategory extends AbstractMagitechRecipeCategory<RecipeHolder<InfusionRecipe>> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei_widgets.png");

    public InfusionRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public InfusionRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.INFUSION_ALTAR)));
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<InfusionRecipe>> getRecipeType() {
        return RecipeHolderTypeInit.INFUSION_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.infuser_infusion");
    }

    @Override
    public Codec<RecipeHolder<InfusionRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(RecipeHolder<InfusionRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public void draw(@NotNull RecipeHolder<InfusionRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.blit(TEXTURE, 26, 4, 0, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 48, 8, 0, 18, 21, 10);
        guiGraphics.blit(TEXTURE, 73, 4, 36, 0, 18, 18);

        RenderHelper.renderFramedText(guiGraphics, Minecraft.getInstance().font, Component.translatable("recipe.magitech.required_mana").append(": " + EnergyFormatter.formatValue(recipe.value().getMana())).getString(), 0, getHeight() - 8, Element.NONE);
    }

    @Override
    public int getWidth() {
        return 155;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<InfusionRecipe> recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {
        Ingredient input = recipe.value().getBase().ingredient();
        ItemStack result = recipe.value().getResultItem(access);
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 5).addIngredients(input);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 5).addItemStack(result);
    }
}
