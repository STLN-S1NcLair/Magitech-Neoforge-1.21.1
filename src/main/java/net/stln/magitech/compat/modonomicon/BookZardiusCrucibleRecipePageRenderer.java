package net.stln.magitech.compat.modonomicon;

import com.klikli_dev.modonomicon.client.gui.book.entry.BookEntryScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.modonomicon.fluid.NeoFluidHolder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.recipe.ZardiusCrucibleRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookZardiusCrucibleRecipePageRenderer extends BookRecipePageRenderer<ZardiusCrucibleRecipe, BookZardiusCrucibleRecipePage> {


    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/book_zardius_crucible.png");

    public BookZardiusCrucibleRecipePageRenderer(BookZardiusCrucibleRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 95;
    }

    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, RecipeHolder<ZardiusCrucibleRecipe> recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        List<FluidStack> inputFluids = new ArrayList<>();

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
        guiGraphics.blit(TEXTURE, recipeX - 10, recipeY + 7, 0, 0, 116, 59, 116, 59);
        int wrap = 4;

        // 入力 ItemStack を取得・セット
        recipe.value().getIngredients().forEach(ing -> {
            if (ing.getItems().length > 0) inputs.add(Arrays.stream(ing.getItems()).toList());
        });
        inputFluids.addAll(Arrays.stream(recipe.value().getInputFluid().getFluids()).toList());

        RegistryAccess registries = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.registryAccess() : null;
        if (registries == null) {
            return;
        }
        ItemStack output = recipe.value().getResultItem(registries);
        FluidStack outputFluid = recipe.value().getOutputFluid();


        for (int i = 0; i < inputs.size(); i++) {
            this.parentScreen.renderItemStacks(guiGraphics, recipeX + (i % wrap) * 19 - 8, recipeY + (i / wrap) * 19 + 9, mouseX, mouseY, inputs.get(i));
        }
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 87, recipeY + 18, mouseX, mouseY, output);
        this.parentScreen.renderItemStack(guiGraphics, recipeX - 8, recipeY + 47, mouseX, mouseY, BlockInit.ZARDIUS_CRUCIBLE_ITEM.toStack());
        this.parentScreen.renderFluidStacks(guiGraphics, recipeX + 48, recipeY + 46, mouseX, mouseY, inputFluids.stream().map(NeoFluidHolder::new).collect(Collectors.toUnmodifiableList()), 2000);
        this.parentScreen.renderFluidStack(guiGraphics, recipeX + 86, recipeY + 46, mouseX, mouseY, new NeoFluidHolder(outputFluid), 2000);
    }
}

