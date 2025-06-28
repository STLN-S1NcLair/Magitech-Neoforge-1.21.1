package net.stln.magitech.util;

import net.stln.magitech.item.tool.material.ToolMaterial;

import java.util.ArrayList;
import java.util.List;

public class ToolMaterialUtil {

    public static List<ToolMaterial> getMaterialCombinationAt(List<ToolMaterial> materials, int partCount, long index) {
        List<ToolMaterial> result = new ArrayList<>();
        int base = materials.size();

        for (int i = 0; i < partCount; i++) {
            int materialIndex = (int)(index % base);
            result.add(materials.get(materialIndex));
            index /= base;
        }

        return result;
    }
}
