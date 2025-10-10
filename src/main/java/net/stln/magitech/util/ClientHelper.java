package net.stln.magitech.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClientHelper {
    // Player
    public static @Nullable Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static @NotNull Optional<Player> getOptionalPlayer() {
        return Optional.ofNullable(getPlayer());
    }

    // Level
    public static @Nullable Level getLevel() {
        var player = getPlayer();
        return player == null ? null : player.level();
    }
    
    // Recipe Manager
    public static @Nullable RecipeManager getRecipeManager() {
        var clientLevel = Minecraft.getInstance().level;
        return clientLevel != null ? clientLevel.getRecipeManager() : null;
    }
    
    public static <I extends RecipeInput, R extends Recipe<I>> @NotNull List<@NotNull R> getAllRecipes(@NotNull Supplier<? extends RecipeType<R>> supplier) {
        RecipeManager recipeManager = getRecipeManager();
        if (recipeManager == null) return List.of();
        return recipeManager.getAllRecipesFor(supplier.get()).stream().map(RecipeHolder::value).toList();
    }

    // Registry Access
    public static @Nullable RegistryAccess getRegistryAccess() {
        var clientLevel = Minecraft.getInstance().level;
        return clientLevel != null ? clientLevel.registryAccess() : null;
    }
}
