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
import net.stln.magitech.content.recipe.ZardiusCrucibleRecipe;
import net.stln.magitech.helper.ClientHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ZardiusCrucibleRecipeCategory extends AbstractMagitechRecipeCategory<RecipeHolder<ZardiusCrucibleRecipe>> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/gui/jei_widgets.png");

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
        int size = recipe.value().getIngredients().size();

        for (int i = 0; i < (size == 0 ? 8 : size); i++) {
            int x, y;

            if (size <= 3) {
                // 横1列（中央寄せ、Y + 9）
                int totalWidth = size * 18;
                int startX = 19 + (36 - totalWidth) / 2; // 中央寄せ（基準幅36）
                x = startX + i * 18;
                y = 4 + 9;
            } else if (size == 4) {
                // 2x2 グリッド（中央寄せ）
                int row = i / 2;
                int col = i % 2;
                x = 19 + col * 18; // 横方向中央に調整
                y = 4 + row * 18;
            } else if (size < 7) {
                // 2列3行（左→右→下に）
                int row = i / 3;
                int col = i % 3;
                int totalWidth = 3 * 18;
                x = 19 + col * 18 + (36 - totalWidth) / 2;
                y = 4 + row * 18;
            } else {
                // 2列で縦並び（なるべく均等）
                int row = i / 4;
                int col = i % 4;
                int totalWidth = 4 * 18;
                x = 19 + col * 18 + (36 - totalWidth) / 2;
                y = 4 + row * 18;
            }

            guiGraphics.blit(TEXTURE, x, y, 0, 0, 18, 18);
        }
        guiGraphics.blit(TEXTURE, 73, 13, 18, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 95, 17, 0, 18, 21, 10);
        var access = ClientHelper.getRegistryAccess();
        if (access == null) return;
        guiGraphics.blit(TEXTURE, 120, 13, 36, 0, 18, 18);
        guiGraphics.blit(TEXTURE, 138, 13, 54, 0, 18, 18);
    }

    @Override
    public int getWidth() {
        return 155;
    }

    @Override
    public int getHeight() {
        return 44;
    }

    @Override
    protected void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<ZardiusCrucibleRecipe> recipe, @NotNull IFocusGroup focuses, @NotNull RecipeManager recipeManager, @NotNull RegistryAccess access) {

        List<Ingredient> ingredients = recipe.value().getIngredients();

        for (int i = 0; i < ingredients.size(); i++) {
            int x, y;

            if (ingredients.size() <= 3) {
                // 横1列（中央寄せ、Y + 9）
                int totalWidth = ingredients.size() * 18;
                int startX = 19 + (36 - totalWidth) / 2; // 中央寄せ（基準幅36）
                x = startX + i * 18;
                y = 4 + 9;
            } else if (ingredients.size() == 4) {
                // 2x2 グリッド（中央寄せ）
                int row = i / 2;
                int col = i % 2;
                x = 19 + col * 18; // 横方向中央に調整
                y = 4 + row * 18;
            } else if (ingredients.size() < 7) {
                // 2列3行（左→右→下に）
                int row = i / 3;
                int col = i % 3;
                int totalWidth = 3 * 18;
                x = 19 + col * 18 + (36 - totalWidth) / 2;
                y = 4 + row * 18;
            } else {
                // 2列で縦並び（なるべく均等）
                int row = i / 4;
                int col = i % 4;
                int totalWidth = 4 * 18;
                x = 19 + col * 18 + (36 - totalWidth) / 2;
                y = 4 + row * 18;
            }

            builder.addSlot(RecipeIngredientRole.INPUT, x + 1, y + 1)
                    .addIngredients(ingredients.get(i));
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 74, 14)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.value().fluidIngredient().getFluids()).toList()).addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    recipeSlotView.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).ifPresent(fluid -> {
                        int amount = fluid.getAmount();
                        // mB単位で表示
                        tooltip.add(Component.literal(amount + " mB").withColor(0x808080));
                    });
                });
        if (!recipe.value().getResultItem(access).isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 14)
                    .addItemStack(recipe.value().getResultItem(access));
        }
        recipe.value().resultFluid().ifPresent(fluidStack -> builder.addSlot(RecipeIngredientRole.OUTPUT, 139, 14)
                .addIngredient(NeoForgeTypes.FLUID_STACK, fluidStack).addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    recipeSlotView.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK).ifPresent(fluid -> {
                        int amount = fluid.getAmount();
                        // mB単位で表示
                        tooltip.add(Component.literal(amount + " mB").withColor(0x808080));
                    });
                }));
    }
}
