package net.stln.magitech.compat.modonomicon;

import com.klikli_dev.modonomicon.client.gui.book.entry.BookEntryScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.klikli_dev.modonomicon.fluid.NeoFluidHolder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.recipe.AthanorPillarInfusionRecipe;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookAthanorPillarInfusionRecipePageRenderer extends BookRecipePageRenderer<AthanorPillarInfusionRecipe, BookAthanorPillarInfusionRecipePage> {


    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/book_athanor_pillar_infusion.png");

    public BookAthanorPillarInfusionRecipePageRenderer(BookAthanorPillarInfusionRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 106;
    }

    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, RecipeHolder<AthanorPillarInfusionRecipe> recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        AthanorPillarInfusionRecipe value = recipe.value();
        List<ItemStack> base = List.of(value.getBase());
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
        guiGraphics.blit(TEXTURE, recipeX - 10, recipeY + 7, 0, 0, 116, 106, 116, 106);
        int wrap = 4;

        // 入力 ItemStack を取得・セット
        value.getIngredients().forEach(ing -> {
            if (ing.getItems().length > 0) inputs.add(Arrays.stream(ing.getItems()).toList());
        });

        RegistryAccess registries = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.registryAccess() : null;
        if (registries == null) {
            return;
        }
        ItemStack output = value.getResultItem(registries);


        for (int i = 0; i < inputs.size(); i++) {
            int x = 0;
            int y = 0;
            switch (i) {
                case 0 -> {x = -8; y = 9;}
                case 1 -> {x = 68; y = 85;}
                case 2 -> {x = -8; y = 85;}
                case 3 -> {x = 68; y = 9;}
                case 4 -> {x = 30; y = 20;}
                case 5 -> {x = 30; y = 74;}
                case 6 -> {x = 3; y = 47;}
                case 7 -> {x = 57; y = 47;}
                case 8 -> {x = 11; y = 28;}
                case 9 -> {x = 49; y = 66;}
                case 10 -> {x = 11; y = 66;}
                case 11 -> {x = 49; y = 28;}
            }
            this.parentScreen.renderItemStacks(guiGraphics, recipeX + x, recipeY + y, mouseX, mouseY, inputs.get(i));
        }
        this.parentScreen.renderItemStacks(guiGraphics, recipeX + 30, recipeY + 47, mouseX, mouseY, base);
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 87, recipeY + 62, mouseX, mouseY, output);
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("recipe.magitech.required_mana").append(": " + value.getMana()),  recipeX - 8, recipeY + 104, 0x004040, false);
        this.parentScreen.renderItemStack(guiGraphics, recipeX + 87, recipeY + 81, mouseX, mouseY, BlockInit.ATHANOR_PILLAR.toStack());
    }
}

