package net.stln.magitech.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.Nullable;

public class JeiHelper {
    public static @Nullable RecipeManager getRecipeManager() {
        var clientLevel = Minecraft.getInstance().level;
        return clientLevel != null ? clientLevel.getRecipeManager() : null;
    }

    public static @Nullable RegistryAccess getRegistryAccess() {
        var clientLevel = Minecraft.getInstance().level;
        return clientLevel != null ? clientLevel.registryAccess() : null;
    }
}
