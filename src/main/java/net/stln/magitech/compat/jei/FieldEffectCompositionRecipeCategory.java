package net.stln.magitech.compat.jei;

import com.mojang.serialization.Codec;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.stln.magitech.Magitech;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.core.api.field_effect.FieldInfluence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FieldEffectCompositionRecipeCategory implements IRecipeCategory<FieldEffectCompositionJeiRecipe> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei/field_composition_recipe.png");
    private static final int SLOT_TEXTURE_X = 128;
    private static final int SLOT_TEXTURE_Y = 0;
    private static final int SLOT_TEXTURE_WIDTH = 24;
    private static final int SLOT_TEXTURE_HEIGHT = 26;
    private static final int SLOT_OFFSET_X = 4;
    private static final int SLOT_OFFSET_Y = 3;
    private static final int SLOT_MARGIN = 2;
    private static final int INPUT_SLOT_Y = 11;
    private final IDrawable icon;

    public FieldEffectCompositionRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, BlockInit.ENVIROMETER.toStack());
    }

    @Override
    public @NotNull RecipeType<FieldEffectCompositionJeiRecipe> getRecipeType() {
        return RecipeHolderTypeInit.FIELD_EFFECT_COMPOSITION_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.field_effect_composition");
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public Codec<FieldEffectCompositionJeiRecipe> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return FieldEffectCompositionJeiRecipe.CODEC;
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(FieldEffectCompositionJeiRecipe recipe) {
        return recipe.id();
    }

    @Override
    public void draw(@NotNull FieldEffectCompositionJeiRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 128, 122);

        List<FieldInfluence> influences = expandInfluences(recipe.influences());
        int slotCount = influences.size();
        int slotsWidth = slotCount * SLOT_TEXTURE_WIDTH + Math.max(0, slotCount - 1) * SLOT_MARGIN;
        int startX = (128 - slotsWidth) / 2;
        for (int index = 0; index < slotCount; index++) {
            int x = startX + index * (SLOT_TEXTURE_WIDTH + SLOT_MARGIN);
            guiGraphics.blit(TEXTURE, x, INPUT_SLOT_Y, SLOT_TEXTURE_X, SLOT_TEXTURE_Y, SLOT_TEXTURE_WIDTH, SLOT_TEXTURE_HEIGHT);
        }
    }

    @Override
    public int getWidth() {
        return 128;
    }

    @Override
    public int getHeight() {
        return 122;
    }

    @Override
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            @NotNull FieldEffectCompositionJeiRecipe recipe,
            @NotNull IFocusGroup focuses
    ) {
        List<FieldInfluence> influences = expandInfluences(recipe.influences());
        int slotCount = influences.size();
        int slotsWidth = slotCount * SLOT_TEXTURE_WIDTH + Math.max(0, slotCount - 1) * SLOT_MARGIN;
        int startX = (128 - slotsWidth) / 2;
        for (int index = 0; index < influences.size(); index++) {
            int x = startX + index * (SLOT_TEXTURE_WIDTH + SLOT_MARGIN);
            builder.addSlot(RecipeIngredientRole.INPUT, x + SLOT_OFFSET_X, INPUT_SLOT_Y + SLOT_OFFSET_Y)
                    .addIngredients(FieldInfluenceIngredient.TYPE, List.of(FieldInfluenceIngredient.of(influences.get(index))));
        }

        ResourceLocation effectId = MagitechRegistries.FIELD_EFFECT_TYPE.getKey(recipe.fieldEffect());
        if (effectId != null) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 88)
                    .addIngredients(FieldEffectIngredient.TYPE, java.util.List.of(FieldEffectIngredient.of(effectId)));
        }
    }

    private static List<FieldInfluence> expandInfluences(List<FieldInfluence> influences) {
        List<FieldInfluence> expanded = new ArrayList<>();
        for (FieldInfluence influence : influences) {
            for (int count = 0; count < Math.max(0, influence.intensity()); count++) {
                expanded.add(new FieldInfluence(influence.type(), 1));
            }
        }
        return expanded;
    }
}
