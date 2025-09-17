package net.stln.magitech.compat.modonomicon;

import com.klikli_dev.modonomicon.client.gui.book.entry.BookEntryScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
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
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ToolAssemblyRecipe;
import net.stln.magitech.recipe.ToolMaterialRecipe;
import net.stln.magitech.util.ToolMaterialUtil;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookToolAssemblyRecipePageRenderer extends BookRecipePageRenderer<ToolAssemblyRecipe, BookToolAssemblyRecipePage> {
    private final List<ItemStack> inputs = new ArrayList<>();
    List<Pair<List<ItemStack>, ItemStack>> displayList = new ArrayList<>();
    private ItemStack output = ItemStack.EMPTY;

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/book_tool_assembly.png");

    public BookToolAssemblyRecipePageRenderer(BookToolAssemblyRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 75;
    }

    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, RecipeHolder<ToolAssemblyRecipe> recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {

        if (!second) {
            if (!this.page.getTitle1().isEmpty()) {
                this.renderTitle(guiGraphics, this.page.getTitle1(), false, BookEntryScreen.PAGE_WIDTH / 2, 0);
            }
        } else {
            if (!this.page.getTitle2().isEmpty()) {
                this.renderTitle(guiGraphics, this.page.getTitle2(), false, BookEntryScreen.PAGE_WIDTH / 2,
                        recipeY - (this.page.getTitle2().getString().isEmpty() ? 10 : 0) - 5);
            }
        }

        RenderSystem.enableBlend();
        guiGraphics.blit(TEXTURE, recipeX - 2, recipeY + 7, 0, 0, 97, 39, 97, 39);
        int wrap = 3;

        // 入力 ItemStack を取得・セット
        recipe.value().getIngredients().forEach(ing -> {
            if (ing.getItems().length > 0) inputs.add(ing.getItems()[0]);
        });

        RegistryAccess registries = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.registryAccess() : null;
        if (registries == null) {
            return;
        }
        output = recipe.value().getResultItem(registries);

        // 初期値を設定
        if (displayList.isEmpty()) {
            setDisplayList();
        }

        // 1秒ごとに表示を切り替え
        int index = (int) ((System.currentTimeMillis() / 1000) % displayList.size());

        Pair<List<ItemStack>, ItemStack> pair = displayList.get(index);

        List<ItemStack> currentInput = pair.getA();
        ItemStack currentOutput = pair.getB();

        for (int i = 0; i < currentInput.size(); i++) {
            this.parentScreen.renderIngredient(guiGraphics, recipeX + (i % wrap) * 19, recipeY + (i / wrap) * 19 + 9, mouseX, mouseY, Ingredient.of(currentInput.get(i)));
        }
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 76, recipeY + 18, mouseX, mouseY, currentOutput);

        this.parentScreen.renderItemStack(guiGraphics, recipeX + 76, recipeY + 38, mouseX, mouseY, BlockInit.ASSEMBLY_WORKBENCH_ITEM.toStack());
    }


    private void setDisplayList() {
        RecipeManager recipeManager = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getRecipeManager() : null;
        if (recipeManager == null) {
            return;
        }
        List<ToolMaterialRecipe> materialRecipes = recipeManager.getAllRecipesFor(RecipeInit.TOOL_MATERIAL_TYPE.get()).stream().map(RecipeHolder::value).toList();
        List<ToolMaterial> materials = materialRecipes.stream().map(m -> ToolMaterialRegister.getMaterial(m.getResultId())).toList();
        int size = inputs.size();
        int indexSize = (int) Math.pow(materials.size(), size);
        List<List<ItemStack>> inputList = new ArrayList<>();
        List<ItemStack> results = new ArrayList<>();
        for (int idx = 0; idx < indexSize; idx += 10) {
            List<ToolMaterial> materialList = ToolMaterialUtil.getMaterialCombinationAt(materials, size, idx);
            List<ItemStack> stacks = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ItemStack returnStack = inputs.get(i).copy();
                returnStack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(materialList.get(i)));
                stacks.add(returnStack);
            }
            inputList.add(stacks);
            ItemStack result = output.copy();
            List<ToolMaterial> reversed = new ArrayList<>(materialList);
            Collections.reverse(reversed);
            result.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(reversed));
            results.add(result);
        }
        for (int i = 0; i < inputList.size(); i++) {
            displayList.add(new Pair<>(inputList.get(i), results.get(i)));
        }
        Collections.shuffle(displayList);
    }
}

