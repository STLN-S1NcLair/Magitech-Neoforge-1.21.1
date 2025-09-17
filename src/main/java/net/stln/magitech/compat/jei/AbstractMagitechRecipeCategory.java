package net.stln.magitech.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.util.ClientHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMagitechRecipeCategory<T extends Recipe<?>> implements IRecipeCategory<T> {
    protected final IDrawable icon;

    protected AbstractMagitechRecipeCategory(IDrawable icon) {
        this.icon = icon;
    }

    @Override
    public final @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public final void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull T recipe, @NotNull IFocusGroup focuses) {
        RecipeManager recipeManager = ClientHelper.getRecipeManager();
        if (recipeManager == null) return;
        var access = ClientHelper.getRegistryAccess();
        if (access == null) return;
        setRecipe(builder, recipe, focuses, recipeManager, access);
    }

    protected abstract void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull T recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access);
}
