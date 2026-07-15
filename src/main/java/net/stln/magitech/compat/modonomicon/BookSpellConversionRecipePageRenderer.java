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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.recipe.SpellConversionRecipe;
import net.stln.magitech.content.recipe.SpellConversionRecipe;
import net.stln.magitech.helper.ComponentHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookSpellConversionRecipePageRenderer extends BookRecipePageRenderer<SpellConversionRecipe, BookSpellConversionRecipePage> {


    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/book_spell_conversion.png");

    public BookSpellConversionRecipePageRenderer(BookSpellConversionRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 88;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, RecipeHolder<SpellConversionRecipe> recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {

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
        int x = recipeX - 8;
        int y = recipeY;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, 112, 80, 112, 80);

        RegistryAccess registries = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.registryAccess() : null;
        if (registries == null) {
            return;
        }

        SpellConversionRecipe value = recipe.value();

        ItemStack threadPage = new ItemStack(ItemInit.THREAD_PAGE.get());
        ComponentHelper.setThreadPage(threadPage, value.spell());

        this.parentScreen.renderItemStack(guiGraphics, x + 80, y + 16, mouseX, mouseY, value.getResultItem(registries));
        this.parentScreen.renderIngredient(guiGraphics, x + 16, y + 16, mouseX, mouseY, value.ingredient());
        this.parentScreen.renderItemStack(guiGraphics, x + 16, y + 48, mouseX, mouseY, threadPage);

        guiGraphics.blit(recipe.value().spell().getIconId(), x + 40, x + 39, 0, 0, 32, 32, 32, 32);
    }
}

