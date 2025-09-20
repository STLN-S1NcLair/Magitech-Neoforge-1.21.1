package net.stln.magitech.compat.modonomicon;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ZardiusCrucibleRecipe;

public class BookZardiusCrucibleRecipePage extends BookRecipePage<ZardiusCrucibleRecipe> {
    public BookZardiusCrucibleRecipePage(BookTextHolder title1, ResourceLocation recipeId1, BookTextHolder title2, ResourceLocation recipeId2, BookTextHolder text, String anchor, BookCondition condition) {
        super(RecipeInit.ZARDIUS_CRUCIBLE_TYPE.get(), title1, recipeId1, title2, recipeId2, text, anchor, condition);
    }

    public static BookZardiusCrucibleRecipePage fromJson(ResourceLocation entryId, JsonObject json, HolderLookup.Provider provider) {
        var common = BookRecipePage.commonFromJson(json, provider);
        var anchor = GsonHelper.getAsString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(entryId, json.getAsJsonObject("condition"), provider)
                : new BookNoneCondition();
        return new BookZardiusCrucibleRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    public static BookZardiusCrucibleRecipePage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        var anchor = buffer.readUtf();
        var condition = BookCondition.fromNetwork(buffer);
        return new BookZardiusCrucibleRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    @Override
    protected ItemStack getRecipeOutput(Level level, RecipeHolder<ZardiusCrucibleRecipe> recipe) {
        if (recipe == null) {
            return ItemStack.EMPTY;
        }

        return recipe.value().getResultItem(level.registryAccess());
    }

    @Override
    public ResourceLocation getType() {
        return PageInit.ZARDIUS_CRUCIBLE_RECIPE;
    }
}
