package net.stln.magitech.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;

import java.util.function.Supplier;

public class RecipeInit {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Magitech.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Magitech.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ToolAssemblyRecipe>> TOOL_ASSEMBLY_TYPE = TYPES.register("tool_assembly", () -> new RecipeType<ToolAssemblyRecipe>() {
        @Override
        public String toString() {
            return "part_tool_assemble";
        }
    });
    public static final Supplier<ToolAssemblyRecipe.Serializer<ToolAssemblyRecipe>> TOOL_ASSEMBLY_SERIALIZER = SERIALIZERS.register("tool_assembly", () -> new ToolAssemblyRecipe.Serializer<>(ToolAssemblyRecipe::new));

    public static final DeferredHolder<RecipeType<?>, RecipeType<PartCuttingRecipe>> PART_CUTTING_TYPE = TYPES.register("part_cutting", () -> new RecipeType<PartCuttingRecipe>() {
        @Override
        public String toString() {
            return "part_cutting";
        }
    });    public static final Supplier<PartCuttingRecipe.Serializer<PartCuttingRecipe>> PART_CUTTING_SERIALIZER = SERIALIZERS.register("part_cutting", () -> new PartCuttingRecipe.Serializer<>(PartCuttingRecipe::new));

    public static final DeferredHolder<RecipeType<?>, RecipeType<ToolMaterialRecipe>> TOOL_MATERIAL_TYPE = TYPES.register("tool_material", () -> new RecipeType<ToolMaterialRecipe>() {
        @Override
        public String toString() {
            return "tool_material";
        }
    });
    public static final DeferredHolder<RecipeType<?>, RecipeType<SpellConversionRecipe>> SPELL_CONVERSION_TYPE = TYPES.register("spell_conversion", () -> new RecipeType<SpellConversionRecipe>() {
        @Override
        public String toString() {
            return "spell_conversion";
        }
    });    public static final Supplier<ToolMaterialRecipe.Serializer<ToolMaterialRecipe>> TOOL_MATERIAL_SERIALIZER = SERIALIZERS.register("tool_material", () -> new ToolMaterialRecipe.Serializer<>(ToolMaterialRecipe::new));

    public static final Supplier<SpellConversionRecipe.Serializer<SpellConversionRecipe>> SPELL_CONVERSION_SERIALIZER = SERIALIZERS.register("spell_conversion", () -> new SpellConversionRecipe.Serializer<>(SpellConversionRecipe::new));

    public static void registerRecipes(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Recipes for " + Magitech.MOD_ID);
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }



}
