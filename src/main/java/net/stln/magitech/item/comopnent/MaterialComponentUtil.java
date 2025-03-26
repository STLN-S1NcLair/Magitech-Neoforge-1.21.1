package net.stln.magitech.item.comopnent;

import net.stln.magitech.item.tool.ToolMaterialDictionary;

public class MaterialComponentUtil {
    public static MaterialComponent generatefromId(String id) {
        return new MaterialComponent(ToolMaterialDictionary.getMaterial(id));
    }
}
