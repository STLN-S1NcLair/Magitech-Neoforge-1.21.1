package net.stln.magitech.compat.patchouli;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.recipe.AthanorPillarInfusionRecipe;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AthanorPillarInfusionRecipeProcessor implements IComponentProcessor {
    private List<List<Ingredient>> inputs = new ArrayList<>();
    private ItemStack base = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;
    private int mana = 0;
    Recipe<?> recipe;
    private String title;
    private String text;

    @Override
    public void setup(Level level, IVariableProvider vars) {
        // "recipe" 変数を渡しても良い
        String recipeId = vars.get("recipe", level.registryAccess()).asString();
        if (vars.has("title")) {
            title = vars.get("title", level.registryAccess()).asString();
        }
        if (vars.has("text")) {
            text = vars.get("text", level.registryAccess()).asString();
        }
         recipe = Minecraft.getInstance().level.getRecipeManager()
            .byKey(ResourceLocation.tryParse(recipeId)).orElseThrow(IllegalArgumentException::new).value();

        if (recipe instanceof AthanorPillarInfusionRecipe r) {
            // 入力 ItemStack を取得・セット
            for (int i = 0; i < 3; i++) {
                inputs.add(r.getInnerIngredients(i));
            }
            mana = r.getMana();
            base = r.getBase();
            output = r.getResultItem(null);
        }
    }

    @Override
    public IVariable process(Level level, String key) {

        if (key.equals("title")) {
            return IVariable.wrap(title == null ? "block.magitech.zardius_crucible" : title, level.registryAccess());
        }
        if (key.equals("text")) {
            return IVariable.wrap(text, level.registryAccess());
        }
        if (key.equals("mana")) {
            return IVariable.wrap(Component.translatable("book.magitech.athanor_pillar_infusion.mana", mana).getString(), level.registryAccess());
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
        return IVariable.wrapList(stacks.stream().map((stack) -> IVariable.from(stack, level.registryAccess())).toList(), level.registryAccess());
    }

}