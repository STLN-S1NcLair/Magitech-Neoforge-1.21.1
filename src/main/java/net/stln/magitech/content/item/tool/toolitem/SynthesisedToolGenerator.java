package net.stln.magitech.content.item.tool.toolitem;

import net.minecraft.world.item.ItemStack;
import net.stln.magitech.content.item.component.ComponentInit;
import net.stln.magitech.content.item.component.MaterialComponent;
import net.stln.magitech.content.item.component.PartMaterialComponent;
import net.stln.magitech.feature.tool.material.ToolMaterial;
import net.stln.magitech.feature.tool.material.ToolMaterialLike;
import net.stln.magitech.feature.tool.part.ToolPart;
import net.stln.magitech.feature.tool.tool_type.ToolType;

import java.util.ArrayList;
import java.util.List;

public class SynthesisedToolGenerator {

    public static ItemStack generatePart(ToolPart part, ToolMaterialLike material) {
        ItemStack stack = new ItemStack(part.partItem().get());
        stack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(material));
        return stack;
    }

    public static ItemStack generateTool(ToolType type, ToolMaterialLike material) {
        ItemStack stack = new ItemStack(type.defaultItem());
        List<ToolMaterial> list = new ArrayList<>();
        ToolMaterial toolMaterial = material.asToolMaterial();
        for (int i = 0; i < type.parts().size(); i++) {
            list.add(toolMaterial);
        }
        stack.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(list));
        return stack;
    }

    public static ItemStack generateTool(ToolType type, ToolMaterialLike... material) {
        ItemStack stack = new ItemStack(type.defaultItem());
        List<ToolMaterial> list = new ArrayList<>();
        for (int i = 0; i < type.parts().size(); i++) {
            ToolMaterial toolMaterial = material[i].asToolMaterial();
            list.add(toolMaterial);
        }
        stack.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(list));
        return stack;
    }
}
