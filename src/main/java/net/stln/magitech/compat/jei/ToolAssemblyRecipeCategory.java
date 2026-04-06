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
import net.stln.magitech.content.item.component.PartMaterialComponent;
import net.stln.magitech.content.recipe.PartCuttingRecipe;
import net.stln.magitech.content.recipe.ToolAssemblyRecipe;
import net.stln.magitech.feature.tool.material.MaterialInit;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.helper.ClientHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ToolAssemblyRecipeCategory extends AbstractMagitechRecipeCategory<RecipeHolder<ToolAssemblyRecipe>> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei_widgets.png");

    public ToolAssemblyRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public ToolAssemblyRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.ASSEMBLY_WORKBENCH)));
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<ToolAssemblyRecipe>> getRecipeType() {
        return RecipeHolderTypeInit.TOOL_ASSEMBLY_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.tool_assembly");
    }

    @Override
    public Codec<RecipeHolder<ToolAssemblyRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(RecipeHolder<ToolAssemblyRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public void draw(@NotNull RecipeHolder<ToolAssemblyRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        int partCount = recipe.value().getIngredients().size();

        for (int i = 0; i < partCount; i++) {
            int x, y;

            if (partCount <= 3) {
                // 横並び：中央寄せ、下にずらす（Y + 9）
                int totalWidth = partCount * 18;
                int startX = 19 + (54 - totalWidth) / 2; // 54 = 3スロット分の幅
                x = startX + i * 18;
                y = 4 + 9; // 通常より9下げる
            } else if (partCount == 4) {
                // 2x2グリッド：中央寄せ
                int row = i / 2;
                int col = i % 2;
                x = 19 + col * 18 + 9; // 横方向中央に調整（+9）
                y = 4 + row * 18;
            } else {
                // デフォルト：3列グリッド
                int row = i / 3;
                int col = i % 3;
                x = 19 + col * 18;
                y = 4 + row * 18;
            }

            guiGraphics.blit(TEXTURE, x, y, 0, 0, 18, 18);
        }

        guiGraphics.blit(TEXTURE, 76, 17, 0, 18, 21, 10);
        guiGraphics.blit(TEXTURE, 101, 13, 36, 0, 18, 18);
    }

    @Override
    public int getWidth() {
        return 137;
    }

    @Override
    public int getHeight() {
        return 44;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<ToolAssemblyRecipe> recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {
        List<ToolMaterial> materials = ClientHelper.getAllCraftableMaterials();

        List<Ingredient> ingredients = recipe.value().getIngredients();

        List<ItemStack> results = new ArrayList<>();
            List<ItemStack> parts = new ArrayList<>();
            List<ToolMaterial> toolMaterials = new ArrayList<>();

        for (Ingredient value : ingredients) {

            ItemStack partStack = value.getItems()[0].copy(); // NOTE: 複数アイテムある場合は適宜対応
            partStack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(MaterialInit.SAMPLE));
            parts.add(partStack);
            toolMaterials.add(MaterialInit.SAMPLE.get());
        }

        // 完成品 ItemStack を生成
        ItemStack resultStack = recipe.value().getResultItem(access).copy();
        resultStack.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(toolMaterials));
        results.add(resultStack);

        List<ItemStack> partInputStacks = new ArrayList<>();

        for (Ingredient ingredient : recipe.value().getIngredients()) {

            ItemStack base = ingredient.getItems()[0].copy();
            base.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(MaterialInit.SAMPLE));

            partInputStacks.add(base);
        }

        // 入力スロット
        int partSize = recipe.value().getIngredients().size();

        for (int i = 0; i < partSize; i++) {
            int x, y;

            if (partSize <= 3) {
                // 横並び：中央寄せ、下にずらす（Y + 9）
                int totalWidth = partSize * 18;
                int startX = 19 + (54 - totalWidth) / 2; // 54 = 3スロット分の幅
                x = startX + i * 18;
                y = 4 + 9; // 通常より9下げる
            } else if (partSize == 4) {
                // 2x2グリッド：中央寄せ
                int row = i / 2;
                int col = i % 2;
                x = 19 + col * 18 + 9; // 横方向中央に調整（+9）
                y = 4 + row * 18;
            } else {
                // デフォルト：3列グリッド
                int row = i / 3;
                int col = i % 3;
                x = 19 + col * 18;
                y = 4 + row * 18;
            }

            builder.addSlot(RecipeIngredientRole.INPUT, x + 1, y + 1).addItemStack(partInputStacks.get(i));
        }

        // 出力スロット（すべての組み合わせ）
        Collections.shuffle(results);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 102, 14).addItemStacks(results);
    }
}
