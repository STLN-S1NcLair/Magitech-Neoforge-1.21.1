package net.stln.magitech.item.component;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;

public class MaterialComponentUtil {
    public static MaterialComponent generatefromId(ResourceLocation id) {
        return new MaterialComponent(ToolMaterialRegister.getMaterial(id));
    }
}
