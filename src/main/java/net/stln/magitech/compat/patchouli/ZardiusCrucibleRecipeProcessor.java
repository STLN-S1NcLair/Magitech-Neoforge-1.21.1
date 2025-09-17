package net.stln.magitech.compat.patchouli;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.stln.magitech.recipe.ZardiusCrucibleRecipe;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZardiusCrucibleRecipeProcessor implements IComponentProcessor {
    private final List<Ingredient> inputs = new ArrayList<>();
    private ItemStack output = ItemStack.EMPTY;
    Recipe<?> recipe;
    private FluidStack fluid;
    private FluidStack outputFluid;
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
    public @NotNull IVariable process(Level level, String key) {
        RegistryAccess access = level.registryAccess();
        switch (key) {
            case "title" -> {
                return IVariable.wrap(title == null ? "block.magitech.zardius_crucible" : title, access);
            }
            case "text" -> {
                return IVariable.wrap(text, access);
            }
            case "input_fluid" -> {
                return IVariable.wrap(Component.translatable("book.magitech.zardius_crucible.input_fluid", Component.translatable(fluid.getDescriptionId()), fluid.getAmount()).getString(), access);
            }
            case "output_fluid" -> {
                return IVariable.wrap(Component.translatable("book.magitech.zardius_crucible.output_fluid", Component.translatable(outputFluid.getDescriptionId()), outputFluid.getAmount()).getString(), access);
            }
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
        return IVariable.wrapList(stacks.stream().map((stack) -> IVariable.from(stack, access)).toList(), access);
    }

}
