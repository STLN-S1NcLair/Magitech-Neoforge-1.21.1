package net.stln.magitech.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RecipeInit {
    // Serializers
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Magitech.MOD_ID);
    public static final Supplier<AthanorPillarInfusionRecipe.Serializer<AthanorPillarInfusionRecipe>> ATHANOR_PILLAR_INFUSION_SERIALIZER = registerSerializer("athanor_pillar_infusion", () -> new AthanorPillarInfusionRecipe.Serializer<>(AthanorPillarInfusionRecipe::new));
    public static final Supplier<RecipeSerializer<PartCuttingRecipe>> PART_CUTTING_SERIALIZER = registerSerializer("part_cutting", PartCuttingRecipe.CODEC, PartCuttingRecipe.STREAM_CODEC);
    public static final Supplier<RecipeSerializer<SpellConversionRecipe>> SPELL_CONVERSION_SERIALIZER = registerSerializer("spell_conversion", SpellConversionRecipe.CODEC, SpellConversionRecipe.STREAM_CODEC);
    public static final Supplier<ToolAssemblyRecipe.Serializer<ToolAssemblyRecipe>> TOOL_ASSEMBLY_SERIALIZER = registerSerializer("tool_assembly", () -> new ToolAssemblyRecipe.Serializer<>(ToolAssemblyRecipe::new));
    public static final Supplier<ToolMaterialRecipe.Serializer<ToolMaterialRecipe>> TOOL_MATERIAL_SERIALIZER = registerSerializer("tool_material", () -> new ToolMaterialRecipe.Serializer<>(ToolMaterialRecipe::new));
    public static final Supplier<RecipeSerializer<ZardiusCrucibleRecipe>> ZARDIUS_CRUCIBLE_SERIALIZER = registerSerializer("zardius_crucible", ZardiusCrucibleRecipe.CODEC, ZardiusCrucibleRecipe.STREAM_CODEC);
    public static final Supplier<RecipeSerializer<InfuserInfusionRecipe>> INFUSER_INFUSION_SERIALIZER = registerSerializer("infuser_infusion", () -> new InfuserInfusionRecipe.Serializer<>(InfuserInfusionRecipe::new));
    // Types
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Magitech.MOD_ID);
    public static final Supplier<RecipeType<AthanorPillarInfusionRecipe>> ATHANOR_PILLAR_INFUSION_TYPE = registerType("athanor_pillar_infusion");
    public static final Supplier<RecipeType<PartCuttingRecipe>> PART_CUTTING_TYPE = registerType("part_cutting");
    public static final Supplier<RecipeType<SpellConversionRecipe>> SPELL_CONVERSION_TYPE = registerType("spell_conversion");
    public static final Supplier<RecipeType<ToolAssemblyRecipe>> TOOL_ASSEMBLY_TYPE = registerType("tool_assembly");
    public static final Supplier<RecipeType<ToolMaterialRecipe>> TOOL_MATERIAL_TYPE = registerType("tool_material");
    public static final Supplier<RecipeType<ZardiusCrucibleRecipe>> ZARDIUS_CRUCIBLE_TYPE = registerType("zardius_crucible");
    public static final Supplier<RecipeType<InfuserInfusionRecipe>> INFUSER_INFUSION_TYPE = registerType("infuser_infusion");

    private static <T extends RecipeSerializer<?>> @NotNull Supplier<T> registerSerializer(@NotNull String name, @NotNull Supplier<T> supplier) {
        return SERIALIZERS.register(name, supplier);
    }

    private static <T extends Recipe<?>> @NotNull Supplier<RecipeSerializer<T>> registerSerializer(@NotNull String name, @NotNull MapCodec<T> codec, @NotNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return SERIALIZERS.register(name, () -> new RecipeSerializer<>() {
            @Override
            public @NotNull MapCodec<T> codec() {
                return codec;
            }

            @Override
            public @NotNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec;
            }
        });
    }

    private static <T extends Recipe<?>> @NotNull Supplier<RecipeType<T>> registerType(@NotNull String name) {
        return TYPES.register(name, RecipeType::simple);
    }

    public static void registerRecipes(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Recipes for " + Magitech.MOD_ID);
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }




}
