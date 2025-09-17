package net.stln.magitech.item.component;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.MagitechRegistries;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;

public class MaterialComponentUtil {
    public static MaterialComponent generatefromId(ResourceLocation id) {
        ToolMaterial material = ToolMaterialRegister.getMaterial(id);
//        ToolMaterial material = MagitechRegistries.TOOL_MATERIAL.get(id);
        if (material == null) {
            throw new IllegalArgumentException("Invalid material id: " + id);
        }
        return new MaterialComponent(material);
    }
}
