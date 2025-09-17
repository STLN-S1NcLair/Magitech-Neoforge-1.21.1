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
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.ThreadPageComponent;
import net.stln.magitech.magic.spell.SpellRegister;
import net.stln.magitech.recipe.SpellConversionRecipe;
import org.jetbrains.annotations.Nullable;

public class SpellConversionRecipeCategory implements IRecipeCategory<SpellConversionRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "spell_conversion");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID,
            "textures/gui/jei_widgets.png");

    public static final RecipeType<SpellConversionRecipe> SPELL_CONVERSION_RECIPE_TYPE =
            new RecipeType<>(UID, SpellConversionRecipe.class);

    private final IDrawable icon;

    public SpellConversionRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemInit.WAND.get()));
    }

    @Override
    public RecipeType<SpellConversionRecipe> getRecipeType() {
        return SPELL_CONVERSION_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.magitech.spell_conversion");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(SpellConversionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        ResourceLocation icon = recipe.getSpell();
        String namespace = icon.getNamespace();
        String path = icon.getPath();
        icon = ResourceLocation.fromNamespaceAndPath(namespace, "textures/spell/" + path + ".png");
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.blit(TEXTURE, 18, 12, 0, 0, 18, 18);
        guiGraphics.blit(icon, 38, 5, 0, 0, 32, 32, 32, 32);
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
    public void setRecipe(IRecipeLayoutBuilder builder, SpellConversionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 13).addIngredients(recipe.getIngredient());

        ItemStack threadPage = new ItemStack(ItemInit.THREAD_PAGE.get());
        threadPage.set(ComponentInit.THREAD_PAGE_COMPONENT, new ThreadPageComponent(SpellRegister.getSpell(recipe.getSpell())));
        builder.addSlot(RecipeIngredientRole.INPUT, 69, 25).addItemStack(threadPage);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 13).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }
}
