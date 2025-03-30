package net.stln.magitech.item.comopnent;

import net.stln.magitech.item.tool.register.ToolMaterialRegister;

public class MaterialComponentUtil {
    public static MaterialComponent generatefromId(String id) {
        return new MaterialComponent(ToolMaterialRegister.getMaterial(id));
    }
}
