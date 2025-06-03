package net.stln.magitech.item.component;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;

import java.util.ArrayList;
import java.util.List;

public class PartMaterialComponentUtil {
    public static PartMaterialComponent generatefromId(List<ResourceLocation> ids) {
        List<ToolMaterial> materials1 = new ArrayList<>();
        for (ResourceLocation id : ids) {
            materials1.add(ToolMaterialRegister.getMaterial(id));
        }
        return new PartMaterialComponent(materials1);
    }
}
