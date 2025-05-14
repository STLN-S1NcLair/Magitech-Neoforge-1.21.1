package net.stln.magitech.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.PartMaterialComponent;
import net.stln.magitech.item.tool.ToolPart;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.partitem.PartItem;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PartToolAssembleRecipe extends CustomRecipe {
    public PartToolAssembleRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean flag = false;
        for (ToolType type : ToolType.values()) {
            flag |= !isCorrectTypesForTool(input, type).isEmpty();
        }
        return flag;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        for (ToolType type : ToolType.values()) {
            List<ToolMaterial> toolMaterials = isCorrectTypesForTool(input, type);
            if (!toolMaterials.isEmpty()) {
                ItemStack stack = new ItemStack(getItemFromType(type));
                stack.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(toolMaterials));
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private Item getItemFromType(ToolType type) {
        return switch (type) {
            case DAGGER -> Items.AIR;
            case LIGHT_SWORD -> ItemInit.LIGHT_SWORD.get();
            case HEAVY_SWORD -> ItemInit.HEAVY_SWORD.get();
            case PICKAXE -> ItemInit.PICKAXE.get();
            case HAMMER -> ItemInit.HAMMER.get();
            case AXE -> Items.AIR;
            case SHOVEL -> Items.AIR;
            case SCYTHE -> ItemInit.SCYTHE.get();
            case SPEAR -> Items.AIR;
            case WAND -> ItemInit.WAND.get();
            case STAFF -> Items.AIR;
            default -> Items.AIR;
        };
    }

    private List<ToolMaterial> isCorrectTypesForTool(CraftingInput input, ToolType type) {
        if (input.ingredientCount() == type.getSize()) {
            boolean flag = true;
            List<ToolMaterial> result = new ArrayList<>(type.getSize());
            for (int i = 0; i < type.getSize(); i++) {
                result.add(null);
            }
            List<ToolPart> partList = new ArrayList<>();
            for (int i = 0; i < type.getSize(); i++) {
                partList.add(ToolMaterialRegister.getToolPartFromIndex(type, i));
            }
            for (int i = 0; i < input.size(); i++) {
                if (input.getItem(i).getItem() instanceof PartItem partItem && input.getItem(i).has(ComponentInit.MATERIAL_COMPONENT.get())) {
                    boolean found = partList.contains(partItem.getPart());
                    int index = partList.indexOf(partItem.getPart());
                    if (found) {
                        ToolMaterial material = input.getItem(i).get(ComponentInit.MATERIAL_COMPONENT.get()).getMaterial();
                        Magitech.LOGGER.info(index + ", " + material.getId() + ", " + partItem);
                        result.set(index, material);
                        partList.set(index, null);
                    }
                    flag &= found;
                }
            }
            if (flag && partList.stream().allMatch(Objects::isNull)) {
                return result;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.PART_TOOL_ASSEMBLE_SERIALIZER.get();
    }
}
