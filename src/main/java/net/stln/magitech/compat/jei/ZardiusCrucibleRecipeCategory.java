package net.stln.magitech.compat.jei;

import com.mojang.serialization.Codec;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
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
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.recipe.InfusionRecipe;
import net.stln.magitech.content.recipe.ZardiusCrucibleRecipe;
import net.stln.magitech.helper.ClientHelper;
import net.stln.magitech.helper.EnergyFormatter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ZardiusCrucibleRecipeCategory extends AbstractMagitechRecipeCategory<RecipeHolder<ZardiusCrucibleRecipe>> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei/zardius_crucible_recipe.png");
    public static final ResourceLocation WIDGETS = Magitech.id("textures/gui/jei_widgets.png");
    protected static long GAUGE_MAX_MANA = 100000; // 表示用の最大マナ量

    public ZardiusCrucibleRecipeCategory(IDrawable icon) {
        super(icon);
    }

    public ZardiusCrucibleRecipeCategory(IGuiHelper helper) {
        this(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.ZARDIUS_CRUCIBLE)));
    }

    @Override
    public @NotNull RecipeType<RecipeHolder<ZardiusCrucibleRecipe>> getRecipeType() {
        return RecipeHolderTypeInit.ZARDIUS_CRUCIBLE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.magitech.zardius_crucible");
    }

    @Override
    public Codec<RecipeHolder<ZardiusCrucibleRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(RecipeHolder<ZardiusCrucibleRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public void draw(@NotNull RecipeHolder<ZardiusCrucibleRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        int size = recipe.value().getSizedIngredients().size();
        long mana = recipe.value().getMana();

        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 128, 170);

        for (int i = 0; i < size; i++) {
            int x = 15, y = 67 + i * 17;
            y -= (size - 1) * 17 / 2; // 中央寄せのためにX座標を調整

            guiGraphics.blit(WIDGETS, x, y, 0, 0, 18, 20);
        }

        int height = (int) ((double) mana / GAUGE_MAX_MANA * 72);
        guiGraphics.blit(TEXTURE, 96, 40 + 72 - height, 128, 0, 16, height);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<ZardiusCrucibleRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
        if (mouseX >= 96 && mouseX <= 112 && mouseY >= 40 && mouseY <= 112) {
            tooltip.add(Component.translatable("recipe.magitech.required_mana").append(Component.literal(": " + EnergyFormatter.formatValue(recipe.value().getMana()))).withColor(0xcdffde));
        }
    }

    @Override
    public int getWidth() {
        return 128;
    }

    @Override
    public int getHeight() {
        return 170;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<ZardiusCrucibleRecipe> recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {
        List<SizedIngredient> ingredients = recipe.value().getSizedIngredients();
        int size = ingredients.size();
        ItemStack result = recipe.value().getResultItem(access);

        for (int i = 0; i < size; i++) {
            int x = 16, y = 68 + i * 17;
            y -= (size - 1) * 17 / 2; // 中央寄せのためにX座標を調整

            builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStacks(List.of(ingredients.get(i).getItems()));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 136).addItemStack(result);

        builder.addSlot(RecipeIngredientRole.INPUT, 56, 32)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.value().getFluidIngredient().getFluids()).toList()).addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    recipeSlotView.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).ifPresent(fluid -> {
                        int amount = fluid.getAmount();
                        // mB単位で表示
                        tooltip.add(Component.literal(amount + " mB").withColor(0x808080));
                    });
                });
        builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 104)
                .addIngredient(NeoForgeTypes.FLUID_STACK, recipe.value().getResultFluid()).addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    recipeSlotView.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).ifPresent(fluid -> {
                        int amount = fluid.getAmount();
                        // mB単位で表示
                        tooltip.add(Component.literal(amount + " mB").withColor(0x808080));
                    });
                });
    }
}
