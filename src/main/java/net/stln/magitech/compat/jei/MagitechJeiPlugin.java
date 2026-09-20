package net.stln.magitech.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.field_effect.effect.FieldEffectInit;
import net.stln.magitech.content.field_effect.effect.RecipeFieldEffectType;
import net.stln.magitech.content.field_effect.influence.FieldInfluenceInit;
import net.stln.magitech.content.gui.PartCuttingScreen;
import net.stln.magitech.content.gui.ToolAssemblyScreen;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.recipe.FieldEffectRecipe;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.core.api.field_effect.FieldEffectType;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import net.stln.magitech.helper.ClientHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class MagitechJeiPlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_UID = Magitech.id("jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        FieldEffectIngredient.register(registration);
        FieldInfluenceIngredient.register(registration);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new PartCuttingRecipeCategory(guiHelper),
                new ToolAssemblyRecipeCategory(guiHelper),
                new SpellConversionRecipeCategory(guiHelper),
                new ZardiusCrucibleRecipeCategory(guiHelper),
                new InfusionRecipeCategory(guiHelper),
                new CrushingRecipeCategory(guiHelper),
                new CompressingRecipeCategory(guiHelper),
                new FieldEffectRecipeCategory(guiHelper),
                new FieldEffectCompositionRecipeCategory(guiHelper),
                new FieldInfluenceSourceRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registration.addRecipes(RecipeHolderTypeInit.PART_CUTTING_TYPE, ClientHelper.getAllRecipes(RecipeInit.PART_CUTTING_TYPE));
        registration.addRecipes(RecipeHolderTypeInit.TOOL_ASSEMBLY_TYPE, ClientHelper.getAllRecipes(RecipeInit.TOOL_ASSEMBLY_TYPE));
        registration.addRecipes(RecipeHolderTypeInit.SPELL_CONVERSION_TYPE, ClientHelper.getAllRecipes(RecipeInit.SPELL_CONVERSION_TYPE));
        registration.addRecipes(RecipeHolderTypeInit.ZARDIUS_CRUCIBLE_TYPE, ClientHelper.getAllRecipes(RecipeInit.ZARDIUS_CRUCIBLE_TYPE));
        registration.addRecipes(RecipeHolderTypeInit.INFUSION_TYPE, ClientHelper.getAllRecipes(RecipeInit.INFUSION_TYPE));
        registration.addRecipes(RecipeHolderTypeInit.CRUSHING_TYPE, ClientHelper.getAllRecipes(RecipeInit.CRUSHING_TYPE));
        registration.addRecipes(RecipeHolderTypeInit.COMPRESSING_TYPE, ClientHelper.getAllRecipes(RecipeInit.COMPRESSING_TYPE));

        List<RecipeHolder<FieldEffectRecipe>> fieldEffectRecipes = new ArrayList<>(ClientHelper.getAllRecipes(RecipeInit.FIELD_EFFECT_TYPE));
        Level level = ClientHelper.getLevel();
        if (level != null) {
            for (var fieldEffectType : MagitechRegistries.FIELD_EFFECT_TYPE) {
                if (fieldEffectType instanceof RecipeFieldEffectType<?, ?> recipeFieldEffectType) {
                    fieldEffectRecipes.addAll(recipeFieldEffectType.getRuntimeFieldEffectRecipes(level));
                }
            }
        }
        registration.addRecipes(RecipeHolderTypeInit.FIELD_EFFECT_TYPE, fieldEffectRecipes);
        registration.addRecipes(RecipeHolderTypeInit.FIELD_EFFECT_COMPOSITION_TYPE, createFieldEffectCompositionRecipes());
        registration.addRecipes(RecipeHolderTypeInit.FIELD_INFLUENCE_SOURCE_TYPE, createFieldInfluenceSourceRecipes());
    }

    private static List<FieldEffectCompositionJeiRecipe> createFieldEffectCompositionRecipes() {
        List<FieldEffectCompositionJeiRecipe> recipes = new ArrayList<>();
        for (FieldEffectType fieldEffect : MagitechRegistries.FIELD_EFFECT_TYPE) {
            ResourceLocation fieldEffectId = MagitechRegistries.FIELD_EFFECT_TYPE.getKey(fieldEffect);
            if (fieldEffectId == null || fieldEffect.getCondition() == null || fieldEffect.getCondition().fieldInfluences().isEmpty()) {
                continue;
            }

            List<FieldInfluence> influences = fieldEffect.getCondition().fieldInfluences().stream()
                    .sorted(Comparator.<FieldInfluence, String>comparing(influence -> {
                        ResourceLocation influenceId = MagitechRegistries.FIELD_INFLUENCE_TYPE.getKey(influence.type());
                        return influenceId == null ? "" : influenceId.toString();
                    }).thenComparingInt(FieldInfluence::intensity))
                    .toList();
            recipes.add(new FieldEffectCompositionJeiRecipe(
                    Magitech.id("field_effect/composition/" + fieldEffectId.getNamespace() + "/" + fieldEffectId.getPath()),
                    fieldEffect,
                    influences
            ));
        }
        return recipes;
    }

    private static List<FieldInfluenceSourceJeiRecipe> createFieldInfluenceSourceRecipes() {
        return List.of(
                new FieldInfluenceSourceJeiRecipe(
                        Magitech.id("field_influence_source/heat_burner"),
                        BlockInit.HEAT_BURNER_ITEM.toStack(),
                        new FieldInfluence(FieldInfluenceInit.HEAT.get(), 1)
                ),
                new FieldInfluenceSourceJeiRecipe(
                        Magitech.id("field_influence_source/chiller"),
                        BlockInit.CHILLER_ITEM.toStack(),
                        new FieldInfluence(FieldInfluenceInit.COOLING.get(), 1)
                )
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(PartCuttingScreen.class, 0, 0, 176, 16, RecipeHolderTypeInit.PART_CUTTING_TYPE);
        registration.addRecipeClickArea(ToolAssemblyScreen.class, 0, 0, 176, 16, RecipeHolderTypeInit.TOOL_ASSEMBLY_TYPE);
        registration.addRecipeClickArea(ToolAssemblyScreen.class, 73, 39, 56, 36, RecipeHolderTypeInit.TOOL_ASSEMBLY_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(BlockInit.ENGINEERING_WORKBENCH.toStack(), RecipeHolderTypeInit.PART_CUTTING_TYPE);
        registration.addRecipeCatalyst(BlockInit.ASSEMBLY_WORKBENCH.toStack(), RecipeHolderTypeInit.TOOL_ASSEMBLY_TYPE);
        registration.addRecipeCatalyst(ItemInit.WAND.toStack(), RecipeHolderTypeInit.SPELL_CONVERSION_TYPE);
        registration.addRecipeCatalyst(BlockInit.ZARDIUS_CRUCIBLE_ITEM.toStack(), RecipeHolderTypeInit.ZARDIUS_CRUCIBLE_TYPE);
        registration.addRecipeCatalyst(BlockInit.INFUSION_ALTAR_ITEM.toStack(), RecipeHolderTypeInit.INFUSION_TYPE);
        registration.addRecipeCatalyst(BlockInit.INFUSER_ITEM.toStack(), RecipeHolderTypeInit.INFUSION_TYPE);
        registration.addRecipeCatalyst(BlockInit.PEDESTAL_PYLON_ITEM.toStack(), RecipeHolderTypeInit.INFUSION_TYPE);
        registration.addRecipeCatalyst(BlockInit.CRUSHER_ITEM.toStack(), RecipeHolderTypeInit.CRUSHING_TYPE);
        registration.addRecipeCatalyst(BlockInit.COMPRESSOR_ITEM.toStack(), RecipeHolderTypeInit.COMPRESSING_TYPE);
    }
}
