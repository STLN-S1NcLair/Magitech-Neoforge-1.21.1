package net.stln.magitech.compat.modonomicon;

import com.klikli_dev.modonomicon.client.gui.book.entry.BookEntryScreen;
import com.klikli_dev.modonomicon.client.render.page.BookRecipePageRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.recipe.InfusionRecipe;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.EnergyFormatter;

import java.util.Arrays;
import java.util.List;

public class BookInfusionRecipePageRenderer extends BookRecipePageRenderer<InfusionRecipe, BookInfusionRecipePage> {


    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/gui/book_infusion.png");
    public static final ResourceLocation WIDGETS = Magitech.id("textures/gui/jei/jei_widgets.png");
    protected static long GAUGE_MAX_MANA = 100000; // 表示用の最大マナ量

    public BookInfusionRecipePageRenderer(BookInfusionRecipePage page) {
        super(page);
    }

    @Override
    protected int getRecipeHeight() {
        return 142;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void drawRecipe(GuiGraphics guiGraphics, RecipeHolder<InfusionRecipe> recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {

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
        int x = recipeX - 12;
        int y = recipeY;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, 120, 136, 136, 136);

        RegistryAccess registries = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.registryAccess() : null;
        if (registries == null) {
            return;
        }

        InfusionRecipe value = recipe.value();

        List<SizedIngredient> ingredients = value.getSizedIngredients();

        int size = ingredients.size();

        for (int i = 0; i < size; i++) {
            int wx = x + 15, wy = y + 51 + i * 17;
            wy -= (size - 1) * 17 / 2; // 中央寄せのためにX座標を調整

            guiGraphics.blit(WIDGETS, wx, wy, 0, 0, 18, 20);
        }

        int height = (int) (Math.min((double) value.getMana() / GAUGE_MAX_MANA * 72, 72));
        guiGraphics.blit(TEXTURE, x + 88, y + 24 + 72 - height, 120, 0, 16, height, 136, 136);

        SizedIngredient base = value.getBase();

        this.parentScreen.renderItemStacks(guiGraphics, x + 52, y + 16, mouseX, mouseY, List.of(base.getItems()));

        for (int i = 0; i < size; i++) {
            int wx = x + 16, wy = y + 52 + i * 17;
            wy -= (size - 1) * 17 / 2; // 中央寄せのためにX座標を調整

            this.parentScreen.renderItemStacks(guiGraphics, wx, wy, mouseX, mouseY, List.of(ingredients.get(i).getItems()));
        }

        this.parentScreen.renderItemStack(guiGraphics, x + 52, y + 104, mouseX, mouseY, value.getResultItem(registries));

        if (mouseX >= x + 88 && mouseX <= x + 104 && mouseY >= y + 24 && mouseY <= y + 96) {
            guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, List.of(Component.translatable("recipe.magitech.required_mana").append(Component.literal(": " + EnergyFormatter.formatValue(value.getMana()))).withColor(0xcdffde)), mouseX, mouseY);
        }
    }


}

