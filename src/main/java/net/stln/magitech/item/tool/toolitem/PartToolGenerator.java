package net.stln.magitech.item.tool.toolitem;

import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.comopnent.ComponentInit;
import net.stln.magitech.item.comopnent.PartMaterialComponent;
import net.stln.magitech.item.tool.material.ToolMaterial;

import java.util.List;

public class PartToolGenerator {

    public static ItemStack generateLightSword(ToolMaterial handle, ToolMaterial blade, ToolMaterial handguard, ToolMaterial toolBinding) {
        ItemStack lightSword = new ItemStack(ItemInit.LIGHT_SWORD.get());
        lightSword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(List.of(handle, blade, handguard, toolBinding)));
        return lightSword;
    }

    public static ItemStack generateHeavySword(ToolMaterial handle, ToolMaterial blade, ToolMaterial toolBinding, ToolMaterial handguard) {
        ItemStack lightSword = new ItemStack(ItemInit.HEAVY_SWORD.get());
        lightSword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(List.of(handle, blade, toolBinding, handguard)));
        return lightSword;
    }
}
