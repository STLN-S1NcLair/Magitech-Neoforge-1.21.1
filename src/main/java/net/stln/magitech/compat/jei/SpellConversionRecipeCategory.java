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
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.recipe.SpellConversionRecipe;
import net.stln.magitech.util.ComponentHelper;
import org.jetbrains.annotations.NotNull;

public class SpellConversionRecipeCategory extends AbstractMagitechRecipeCategory<SpellConversionRecipe> {
    public static final ResourceLocation UID = Magitech.id("spell_conversion");
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei_widgets.png");

    public static final RecipeType<SpellConversionRecipe> SPELL_CONVERSION_RECIPE_TYPE = new RecipeType<>(UID, SpellConversionRecipe.class);

    public SpellConversionRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public SpellConversionRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemInit.WAND.get())));
    }

    @Override
    public @NotNull RecipeType<SpellConversionRecipe> getRecipeType() {
        return SPELL_CONVERSION_RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.spell_conversion");
    }

    @Override
    public void draw(@NotNull SpellConversionRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.blit(TEXTURE, 18, 12, 0, 0, 18, 18);
        guiGraphics.blit(recipe.spell().getIconId(), 38, 5, 0, 0, 32, 32, 32, 32);
        guiGraphics.blit(TEXTURE, 68, 24, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 74, 16, 0, 18, 21, 10);
        guiGraphics.blit(TEXTURE, 99, 12, 36, 0, 18, 18);
    }

    @Override
    public int getWidth() {
        return 135;
    }

    @Override
    public int getHeight() {
        return 42;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull SpellConversionRecipe recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 13).addIngredients(recipe.ingredient());

        ItemStack threadPage = new ItemStack(ItemInit.THREAD_PAGE.get());
        ComponentHelper.setThreadPage(threadPage, recipe.spell());
        builder.addSlot(RecipeIngredientRole.INPUT, 69, 25).addItemStack(threadPage);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 13).addItemStack(recipe.getResultItem(access));
    }
}
