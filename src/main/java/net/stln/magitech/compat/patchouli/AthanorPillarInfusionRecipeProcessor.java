package net.stln.magitech.compat.patchouli;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.stln.magitech.recipe.AthanorPillarInfusionRecipe;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AthanorPillarInfusionRecipeProcessor implements IComponentProcessor {
    private final List<List<Ingredient>> inputs = new ArrayList<>();
    private ItemStack base = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;
    private int mana = 0;
    Recipe<?> recipe;
    private String title;
    private String text;

    @Override
    public void setup(Level level, IVariableProvider vars) {
        RegistryAccess access = level.registryAccess();
        // "recipe" 変数を渡しても良い
        String recipeId = vars.get("recipe", access).asString();
        if (vars.has("title")) {
            title = vars.get("title", access).asString();
        }
        if (vars.has("text")) {
            text = vars.get("text", access).asString();
        }
        recipe = level.getRecipeManager().byKey(ResourceLocation.parse(recipeId)).orElseThrow(IllegalArgumentException::new).value();

        if (recipe instanceof AthanorPillarInfusionRecipe r) {
            // 入力 ItemStack を取得・セット
            for (int i = 0; i < 3; i++) {
                inputs.add(r.getInnerIngredients(i));
            }
            mana = r.getMana();
            base = r.getBase();
            output = r.getResultItem(access);
        }
    }

    @Override
    public @NotNull IVariable process(Level level, String key) {
        RegistryAccess access = level.registryAccess();
        switch (key) {
            case "title" -> {
                return IVariable.wrap(title == null ? "block.magitech.zardius_crucible" : title, access);
            }
            case "text" -> {
                return IVariable.wrap(text, access);
            }
            case "mana" -> {
                return IVariable.wrap(Component.translatable("book.magitech.athanor_pillar_infusion.mana", mana).getString(), access);
            }
        }
        int size = 0;
        for (List<Ingredient> input : inputs) {
            size += input.size();
        }
        List<ItemStack> stacks = new ArrayList<>();
        Ingredient returnStack = Ingredient.EMPTY;
        for (int i = 0; i < 12; i++) {
            if (key.equals("input" + i) && size > i) {
                try {
                    returnStack = inputs.get(i / 4).get(i % 4);
                } catch (IndexOutOfBoundsException e) {
                    returnStack = Ingredient.EMPTY;
                }
            }
        }
        if (key.equals("base")) {
            returnStack = Ingredient.of(base.copy());
        }
        if (key.equals("result")) {
            returnStack = Ingredient.of(output.copy());
        }
        stacks.addAll(List.of(returnStack.getItems()));
        Collections.shuffle(stacks);
        return IVariable.wrapList(stacks.stream().map((stack) -> IVariable.from(stack, access)).toList(), access);
    }

}
