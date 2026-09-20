package net.stln.magitech.compat.jei;

import com.mojang.serialization.Codec;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.recipe.FieldEffectRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class FieldEffectRecipeCategory extends AbstractMagitechRecipeCategory<RecipeHolder<FieldEffectRecipe>> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei/field_effect_recipe.png");

    public FieldEffectRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public FieldEffectRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, BlockInit.ENVIROMETER.toStack()));
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<FieldEffectRecipe>> getRecipeType() {
        return RecipeHolderTypeInit.FIELD_EFFECT_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.field_effect");
    }

    @Override
    public Codec<RecipeHolder<FieldEffectRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(RecipeHolder<FieldEffectRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public void draw(@NotNull RecipeHolder<FieldEffectRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 112, 74);
    }

    @Override
    public int getWidth() {
        return 112;
    }

    @Override
    public int getHeight() {
        return 74;
    }

    @Override
    protected void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            @NotNull RecipeHolder<FieldEffectRecipe> recipe,
            @NotNull IFocusGroup focuses,
            @NotNull RecipeManager recipeManager,
            @NotNull RegistryAccess access
    ) {
        FieldEffectRecipe value = recipe.value();
        value.getIngredient().ifPresent(ingredient -> addItemInput(builder, ingredient));
        value.getFluidIngredient().ifPresent(ingredient -> builder.addSlot(RecipeIngredientRole.INPUT, 16, 16)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(ingredient.getFluids()).toList()));

        ResourceLocation effectId = MagitechRegistries.FIELD_EFFECT_TYPE.getKey(value.getFieldEffect());
        if (effectId != null) {
            builder.addSlot(RecipeIngredientRole.INPUT, 48, 40)
                    .addIngredients(FieldEffectIngredient.TYPE, List.of(FieldEffectIngredient.of(effectId)));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 16).addItemStacks(value.getResults());
        value.getFluidResult().ifPresent(fluid -> builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 16)
                .addIngredient(NeoForgeTypes.FLUID_STACK, fluid));
    }

    private static void addItemInput(IRecipeLayoutBuilder builder, SizedIngredient ingredient) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 16).addItemStacks(List.of(ingredient.getItems()));
    }
}
