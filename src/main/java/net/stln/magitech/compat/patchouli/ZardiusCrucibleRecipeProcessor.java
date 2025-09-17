package net.stln.magitech.compat.patchouli;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.stln.magitech.recipe.ZardiusCrucibleRecipe;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZardiusCrucibleRecipeProcessor implements IComponentProcessor {
    Recipe<?> recipe;
    private List<Ingredient> inputs = new ArrayList<>();
    private ItemStack output = ItemStack.EMPTY;
    private SizedFluidIngredient fluid;
    private FluidStack outputFluid;
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

        if (recipe instanceof ZardiusCrucibleRecipe r) {
            // 入力 ItemStack を取得・セット
            r.getIngredients().forEach(ing -> {
                if (ing.getItems().length > 0) inputs.add(ing);
            });
            fluid = r.getInputFluid();
            output = r.getResultItem(null);
            outputFluid = r.getOutputFluid();
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
        if (key.equals("input_fluid")) {
            long time = Minecraft.getInstance().level.getGameTime() / 20;
            int index = (int) (time % fluid.getFluids().length);
            FluidStack stack = fluid.getFluids()[index];
            return IVariable.wrap(Component.translatable("book.magitech.zardius_crucible.input_fluid", Component.translatable(stack.getDescriptionId()), stack.getAmount()).getString(), level.registryAccess());
        }
        if (key.equals("output_fluid")) {
            return IVariable.wrap(Component.translatable("book.magitech.zardius_crucible.output_fluid", Component.translatable(outputFluid.getDescriptionId()), outputFluid.getAmount()).getString(), level.registryAccess());
        }
        int size = inputs.size();
        List<ItemStack> stacks = new ArrayList<>();
        Ingredient returnStack = Ingredient.EMPTY;
        for (int i = 0; i < 8; i++) {
            if (key.equals("input" + i) && size > i) {
                returnStack = inputs.get(i);
            }
        }
        if (key.equals("result")) {
            returnStack = Ingredient.of(output.copy());
        }
        stacks.addAll(List.of(returnStack.getItems()));
        Collections.shuffle(stacks);
        return IVariable.wrapList(stacks.stream().map((stack) -> IVariable.from(stack, level.registryAccess())).toList(), level.registryAccess());
    }

}