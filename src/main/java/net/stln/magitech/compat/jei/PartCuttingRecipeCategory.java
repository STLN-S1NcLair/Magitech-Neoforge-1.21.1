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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.content.item.component.MaterialComponent;
import net.stln.magitech.content.recipe.PartCuttingRecipe;
import net.stln.magitech.content.recipe.RecipeInit;
import net.stln.magitech.content.recipe.ToolMaterialRecipe;
import net.stln.magitech.feature.tool.material.MaterialInit;
import net.stln.magitech.helper.ClientHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PartCuttingRecipeCategory extends AbstractMagitechRecipeCategory<RecipeHolder<PartCuttingRecipe>> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei/part_cutting_recipe.png");

    public PartCuttingRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public PartCuttingRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.ENGINEERING_WORKBENCH)));
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<PartCuttingRecipe>> getRecipeType() {
        return RecipeHolderTypeInit.PART_CUTTING_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.part_cutting");
    }

    @Override
    public Codec<RecipeHolder<PartCuttingRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(RecipeHolder<PartCuttingRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public void draw(@NotNull RecipeHolder<PartCuttingRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 112, 50);
    }

    @Override
    public int getWidth() {
        return 112;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<PartCuttingRecipe> recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {
        List<ToolMaterialRecipe> materialRecipes = ClientHelper.getAllRecipes(RecipeInit.TOOL_MATERIAL_TYPE).stream().map(RecipeHolder::value).toList();
        List<ItemStack> inputs = new ArrayList<>();
        for (ToolMaterialRecipe materialRecipe : materialRecipes) {
            Ingredient ingredient = materialRecipe.getIngredients().getFirst();
            for (ItemStack itemStack : ingredient.getItems()) {
                if (itemStack.isEmpty()) continue;
                inputs.add(itemStack.copyWithCount(recipe.value().inputCount()));
            }
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 16).addItemStacks(inputs);

        ItemStack resultStack = recipe.value().getResultItem(access).copy();
        resultStack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(MaterialInit.SAMPLE));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 16).addItemStack(resultStack);
    }
}
