package net.stln.magitech.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.ItemTagKeys;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.feature.magic.spell.SpellInit;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ModComponentRecipeProvider implements DataProvider {
    private final Path recipePath;

    public ModComponentRecipeProvider(PackOutput output) {
        this.recipePath = output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(Magitech.MOD_ID + "/recipe/crafting/" + SpellInit.ENERCRUX.getId().getPath() + ".json");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shapeless");
        recipe.addProperty("category", "misc");

        JsonArray ingredients = new JsonArray();
        ingredients.add(item(Items.PAPER));
        ingredients.add(item(Items.STRING));
        ingredients.add(item(Items.STRING));
        ingredients.add(item(Items.GLASS));
        JsonObject fluorite = new JsonObject();
        fluorite.addProperty("tag", ItemTagKeys.GEMS_FLUORITE.location().toString());
        ingredients.add(fluorite);
        recipe.add("ingredients", ingredients);

        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("id", ItemInit.THREAD_PAGE.getId().toString());
        JsonObject components = new JsonObject();
        components.addProperty(ComponentInit.THREAD_PAGE_COMPONENT_ID.toString(), SpellInit.ENERCRUX.getId().toString());
        result.add("components", components);
        recipe.add("result", result);

        return DataProvider.saveStable(cache, recipe, recipePath);
    }

    @Override
    public String getName() {
        return "Component recipes: " + Magitech.MOD_ID;
    }

    private static JsonObject item(net.minecraft.world.level.ItemLike itemLike) {
        JsonObject item = new JsonObject();
        item.addProperty("item", net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(itemLike.asItem()).toString());
        return item;
    }
}
