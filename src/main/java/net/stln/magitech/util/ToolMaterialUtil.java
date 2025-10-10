package net.stln.magitech.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.stln.magitech.item.ItemTagKeys;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.recipe.RecipeInit;
import net.stln.magitech.recipe.ToolMaterialRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ToolMaterialUtil {

    public static @NotNull List<ToolMaterial> getMaterialCombinationAt(@NotNull List<ToolMaterial> materials, int partCount, long index) {
        List<ToolMaterial> result = new ArrayList<>();
        int base = materials.size();

        for (int i = 0; i < partCount; i++) {
            int materialIndex = (int) (index % base);
            result.add(materials.get(materialIndex));
            index /= base;
        }

        return result;
    }

    public static boolean isCorrectMaterialForUpgrade(int tier, int point, @NotNull Item item) {
        return item.getDefaultInstance().is(getUpgradeMaterialTag(tier, point));
    }

    public static TagKey<Item> getUpgradeMaterialTag(int tier, int point) {
        return switch (tier - point) {
            case 0, 1, 2, 3, 4 -> ItemTagKeys.UPGRADE_MATERIAL_0;
            case 5, 6, 7, 8, 9 -> ItemTagKeys.UPGRADE_MATERIAL_5;
            case 10, 11, 12, 13, 14 -> ItemTagKeys.UPGRADE_MATERIAL_10;
            case 15, 16, 17, 18, 19 -> ItemTagKeys.UPGRADE_MATERIAL_15;
            default -> ItemTagKeys.UPGRADE_MATERIAL_20;
        };
    }
}
