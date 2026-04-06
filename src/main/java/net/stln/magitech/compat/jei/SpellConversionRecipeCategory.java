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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.recipe.PartCuttingRecipe;
import net.stln.magitech.content.recipe.SpellConversionRecipe;
import net.stln.magitech.helper.ComponentHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpellConversionRecipeCategory extends AbstractMagitechRecipeCategory<RecipeHolder<SpellConversionRecipe>> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei_widgets.png");

    public SpellConversionRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public SpellConversionRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemInit.WAND.get())));
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<SpellConversionRecipe>> getRecipeType() {
        return RecipeHolderTypeInit.SPELL_CONVERSION_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.spell_conversion");
    }

    @Override
    public Codec<RecipeHolder<SpellConversionRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(RecipeHolder<SpellConversionRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public void draw(@NotNull RecipeHolder<SpellConversionRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.blit(TEXTURE, 18, 12, 0, 0, 18, 18);
        guiGraphics.blit(recipe.value().spell().getIconId(), 38, 5, 0, 0, 32, 32, 32, 32);
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
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<SpellConversionRecipe> recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 13).addIngredients(recipe.value().ingredient());

        ItemStack threadPage = new ItemStack(ItemInit.THREAD_PAGE.get());
        ComponentHelper.setThreadPage(threadPage, recipe.value().spell());
        builder.addSlot(RecipeIngredientRole.INPUT, 69, 25).addItemStack(threadPage);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 13).addItemStack(recipe.value().getResultItem(access));
    }
}
