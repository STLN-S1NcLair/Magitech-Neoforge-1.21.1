package net.stln.magitech.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;

import java.util.function.Supplier;

public class RecipeInit {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Magitech.MOD_ID);

    public static final Supplier<SimpleCraftingRecipeSerializer<PartToolAssembleRecipe>> PART_TOOL_ASSEMBLE_SERIALIZER = SERIALIZERS.register("crafting_part_tool_assemble", () -> new SimpleCraftingRecipeSerializer<>(PartToolAssembleRecipe::new));

    public static void registerRecipes(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Recipes for " + Magitech.MOD_ID);
        SERIALIZERS.register(eventBus);
    }
}
