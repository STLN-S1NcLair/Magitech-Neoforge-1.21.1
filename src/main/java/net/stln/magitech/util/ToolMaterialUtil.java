package net.stln.magitech.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.stln.magitech.item.tool.material.ToolMaterial;

import java.util.ArrayList;
import java.util.List;

public class ToolMaterialUtil {

    public static List<ToolMaterial> getMaterialCombinationAt(List<ToolMaterial> materials, int partCount, long index) {
        List<ToolMaterial> result = new ArrayList<>();
        int base = materials.size();

        for (int i = 0; i < partCount; i++) {
            int materialIndex = (int) (index % base);
            result.add(materials.get(materialIndex));
            index /= base;
        }

        return result;
    }

    public static boolean isCorrectMaterialForUpgrade(int tier, int point, Item item) {
        return item.getDefaultInstance().is(getUpgradeMaterialTag(tier, point));
    }

    public static TagKey<Item> getUpgradeMaterialTag(int tier, int point) {
        return switch (tier - point) {
            case 0, 1, 2, 3, 4 -> net.stln.magitech.item.ItemTagKeys.UPGRADE_MATERIAL_0;
            case 5, 6, 7, 8, 9 -> net.stln.magitech.item.ItemTagKeys.UPGRADE_MATERIAL_5;
            case 10, 11, 12, 13, 14 -> net.stln.magitech.item.ItemTagKeys.UPGRADE_MATERIAL_10;
            case 15, 16, 17, 18, 19 -> net.stln.magitech.item.ItemTagKeys.UPGRADE_MATERIAL_15;
            default -> net.stln.magitech.item.ItemTagKeys.UPGRADE_MATERIAL_20;
        };
    }
}
