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
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei/spell_conversion_recipe.png");

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
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 144, 82);
        guiGraphics.blit(recipe.value().spell().getIconId(), 56, 40, 0, 0, 32, 32, 32, 32);
    }

    @Override
    public int getWidth() {
        return 144;
    }

    @Override
    public int getHeight() {
        return 82;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<SpellConversionRecipe> recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 16).addIngredients(recipe.value().ingredient());

        ItemStack threadPage = new ItemStack(ItemInit.THREAD_PAGE.get());
        ComponentHelper.setThreadPage(threadPage, recipe.value().spell());
        builder.addSlot(RecipeIngredientRole.CATALYST, 32, 48).addItemStack(threadPage);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 16).addItemStack(recipe.value().getResultItem(access));
    }
}
