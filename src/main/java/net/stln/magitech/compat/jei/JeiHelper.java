package net.stln.magitech.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class JeiHelper {
    public static @Nullable RecipeManager getRecipeManager() {
        var clientLevel = Minecraft.getInstance().level;
        return clientLevel != null ? clientLevel.getRecipeManager() : null;
    }

    public static @Nullable RegistryAccess getRegistryAccess() {
        var clientLevel = Minecraft.getInstance().level;
        return clientLevel != null ? clientLevel.registryAccess() : null;
    }

    public static <I extends RecipeInput, R extends Recipe<I>> @NotNull List<@NotNull R> getAllRecipes(@NotNull Supplier<? extends RecipeType<R>> supplier) {
        RecipeManager recipeManager = getRecipeManager();
        if (recipeManager == null) return List.of();
        return recipeManager.getAllRecipesFor(supplier.get()).stream().map(RecipeHolder::value).toList();
    }
}
