package net.stln.magitech.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.recipe.*;
import org.jetbrains.annotations.NotNull;

public class RecipeHolderTypeInit {

    public static final RecipeType<RecipeHolder<PartCuttingRecipe>> PART_CUTTING_TYPE = createJeiHolderType("part_cutting", PartCuttingRecipe.class);
    public static final RecipeType<RecipeHolder<SpellConversionRecipe>> SPELL_CONVERSION_TYPE = createJeiHolderType("spell_conversion", SpellConversionRecipe.class);
    public static final RecipeType<RecipeHolder<ToolAssemblyRecipe>> TOOL_ASSEMBLY_TYPE = createJeiHolderType("tool_assembly", ToolAssemblyRecipe.class);
    public static final RecipeType<RecipeHolder<ToolMaterialRecipe>> TOOL_MATERIAL_TYPE = createJeiHolderType("tool_material", ToolMaterialRecipe.class);
    public static final RecipeType<RecipeHolder<ZardiusCrucibleRecipe>> ZARDIUS_CRUCIBLE_TYPE = createJeiHolderType("zardius_crucible", ZardiusCrucibleRecipe.class);
    public static final RecipeType<RecipeHolder<InfusionRecipe>> INFUSION_TYPE = createJeiHolderType("infusion", InfusionRecipe.class);
    public static final RecipeType<RecipeHolder<CrushingRecipe>> CRUSHING_TYPE = createJeiHolderType("crushing", CrushingRecipe.class);
    public static final RecipeType<RecipeHolder<CompressingRecipe>> COMPRESSING_TYPE = createJeiHolderType("compressing", CompressingRecipe.class);
    public static final RecipeType<RecipeHolder<FieldEffectRecipe>> FIELD_EFFECT_TYPE = createJeiHolderType("field_effect", FieldEffectRecipe.class);
    public static final RecipeType<FieldEffectCompositionJeiRecipe> FIELD_EFFECT_COMPOSITION_TYPE = mezz.jei.api.recipe.RecipeType.create(Magitech.MOD_ID, "field_effect_composition", FieldEffectCompositionJeiRecipe.class);
    public static final RecipeType<FieldInfluenceSourceJeiRecipe> FIELD_INFLUENCE_SOURCE_TYPE = mezz.jei.api.recipe.RecipeType.create(Magitech.MOD_ID, "field_influence_source", FieldInfluenceSourceJeiRecipe.class);

    private static <T extends Recipe<?>> @NotNull RecipeType<RecipeHolder<T>> createJeiHolderType(@NotNull String name, Class<T> t) {
        Class<? extends RecipeHolder<T>> holderClass = (Class<? extends RecipeHolder<T>>) (Class<?>) RecipeHolder.class;
        return mezz.jei.api.recipe.RecipeType.create(Magitech.MOD_ID, name, holderClass);
    }
}
