package net.stln.magitech.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.MaterialComponent;
import net.stln.magitech.item.component.PartMaterialComponent;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.recipe.PartCuttingRecipe;
import net.stln.magitech.recipe.ToolAssemblyRecipe;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ToolMaterialRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToolAssemblyRecipeCategory implements IRecipeCategory<ToolAssemblyRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "tool_assembly");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID,
            "textures/gui/jei_widgets.png");

    public static final RecipeType<ToolAssemblyRecipe> TOOL_ASSEMBLY_RECIPE_TYPE =
            new RecipeType<>(UID, ToolAssemblyRecipe.class);

    private final IDrawable icon;

    public ToolAssemblyRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.ASSEMBLY_WORKBENCH));
    }

    @Override
    public RecipeType<ToolAssemblyRecipe> getRecipeType() {
        return TOOL_ASSEMBLY_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.magitech.tool_assembly");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(ToolAssemblyRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        int partCount = recipe.getIngredients().size();

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
    public void setRecipe(IRecipeLayoutBuilder builder, ToolAssemblyRecipe recipe, IFocusGroup focuses) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<ToolMaterialRecipe> materialRecipes = recipeManager.getAllRecipesFor(RecipeInit.TOOL_MATERIAL_TYPE.get()).stream().map(RecipeHolder::value).toList();
        List<ToolMaterial> materials = materialRecipes.stream()
                .map(m -> ToolMaterialRegister.getMaterial(m.getResultId()))
                .toList();

        List<Ingredient> ingredients = recipe.getIngredients();
        int partCount = ingredients.size();
        int materialCount = materials.size();

        // デカルト積 (m^n通り)
        List<ItemStack> results = new ArrayList<>();
        int totalCombinations = (int) Math.pow(materialCount, partCount);

        for (int i = 0; i < totalCombinations; i++) {
            int index = i;
            List<ItemStack> parts = new ArrayList<>();
            List<ToolMaterial> toolMaterials = new ArrayList<>();

            for (int j = 0; j < partCount; j++) {
                int materialIndex = index % materialCount;
                index /= materialCount;

                Ingredient ingredient = ingredients.get(j);
                ItemStack partStack = ingredient.getItems()[0].copy(); // NOTE: 複数アイテムある場合は適宜対応
                partStack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(materials.get(materialIndex)));
                parts.add(partStack);
                toolMaterials.add(materials.get(materialIndex));
            }

            // 完成品 ItemStack を生成
            ItemStack resultStack = recipe.getResultItem(null).copy();
            resultStack.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(toolMaterials));
            results.add(resultStack);
        }
        // 各パーツに対して、全ToolMaterialを使った ItemStack を用意
        List<List<ItemStack>> partInputStacks = new ArrayList<>();

        for (Ingredient ingredient : recipe.getIngredients()) {
            List<ItemStack> variants = new ArrayList<>();
            for (ToolMaterial material : materials) {
                // 複数アイテムに対応する場合は ingredient.getItems() をループすることも可能
                ItemStack base = ingredient.getItems()[0].copy();
                base.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(material));
                variants.add(base);
            }
            partInputStacks.add(variants); // 1パーツに対する複数素材のスタック
        }

        // 入力スロット
        int partSize = recipe.getIngredients().size();

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

            builder.addSlot(RecipeIngredientRole.INPUT, x + 1, y + 1)
                    .addItemStacks(partInputStacks.get(i)); // 全素材のリストを渡す
        }

        // 出力スロット（すべての組み合わせ）
        Collections.shuffle(results);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 102, 14)
                .addItemStacks(results);
    }
}
