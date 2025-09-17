package net.stln.magitech.compat.patchouli;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.MaterialComponent;
import net.stln.magitech.item.component.PartMaterialComponent;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ToolAssemblyRecipe;
import net.stln.magitech.recipe.ToolMaterialRecipe;
import net.stln.magitech.util.ToolMaterialUtil;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToolAssemblyRecipeProcessor implements IComponentProcessor {
    Recipe<?> recipe;
    String title;
    String text;
    private List<ItemStack> inputs = new ArrayList<>();
    private ItemStack output = ItemStack.EMPTY;

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

        if (recipe instanceof ToolAssemblyRecipe r) {
            // 入力 ItemStack を取得・セット
            r.getIngredients().forEach(ing -> {
                if (ing.getItems().length > 0) inputs.add(ing.getItems()[0]);
            });
            output = r.getResultItem(null);
        }
    }

    @Override
    public IVariable process(Level level, String key) {

        if (key.equals("title")) {
            return IVariable.wrap(title == null ? "block.magitech.assembly_workbench" : title, level.registryAccess());
        }
        if (key.equals("text")) {
            return IVariable.wrap(text, level.registryAccess());
        }
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<ToolMaterialRecipe> materialRecipes = recipeManager.getAllRecipesFor(RecipeInit.TOOL_MATERIAL_TYPE.get()).stream().map(RecipeHolder::value).toList();
        List<ToolMaterial> materials = materialRecipes.stream().map(m -> ToolMaterialRegister.getMaterial(m.getResultId())).toList();
        int size = inputs.size();
        int indexSize = (int) Math.pow(materials.size(), size);
        List<ItemStack> stacks = new ArrayList<>();
        for (int idx = 0; idx < indexSize; idx += 10) {
            List<ToolMaterial> materialList = ToolMaterialUtil.getMaterialCombinationAt(materials, size, idx);
            ItemStack returnStack = ItemStack.EMPTY;
            for (int i = 0; i < 6; i++) {
                if (key.equals("input" + i) && size > i) {
                    returnStack = inputs.get(i).copy();
                    returnStack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(materialList.get(i)));
                }
            }
            if (key.equals("result")) {
                returnStack = output.copy();
                returnStack.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(materialList));
            }
            stacks.add(returnStack);
        }
        Collections.shuffle(stacks);
        return IVariable.wrapList(stacks.stream().map((stack) -> IVariable.from(stack, level.registryAccess())).toList(), level.registryAccess());
    }

}