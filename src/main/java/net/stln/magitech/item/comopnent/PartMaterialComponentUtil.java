package net.stln.magitech.item.comopnent;

import net.stln.magitech.item.tool.ToolMaterial;
import net.stln.magitech.item.tool.ToolMaterialDictionary;

import java.util.ArrayList;
import java.util.List;

public class PartMaterialComponentUtil {
    public static PartMaterialComponent generatefromId(List<String> ids) {
        List<ToolMaterial> materials1 = new ArrayList<>();
        for (String id : ids) {
            materials1.add(ToolMaterialDictionary.getMaterial(id));
        }
        return new PartMaterialComponent(materials1);
    }
}
