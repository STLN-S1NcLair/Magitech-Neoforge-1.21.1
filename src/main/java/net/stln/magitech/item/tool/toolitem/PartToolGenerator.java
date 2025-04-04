package net.stln.magitech.item.tool.toolitem;

import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.PartMaterialComponent;
import net.stln.magitech.item.tool.material.ToolMaterial;

import java.util.List;

public class PartToolGenerator {

    public static ItemStack generateLightSword(ToolMaterial handle, ToolMaterial blade, ToolMaterial handguard, ToolMaterial toolBinding) {
        ItemStack lightSword = new ItemStack(ItemInit.LIGHT_SWORD.get());
        lightSword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(List.of(handle, blade, handguard, toolBinding)));
        return lightSword;
    }

    public static ItemStack generateLightSword(ToolMaterial material) {
        return generateLightSword(material, material, material, material);
    }

    public static ItemStack generateHeavySword(ToolMaterial handle, ToolMaterial blade, ToolMaterial toolBinding, ToolMaterial handguard) {
        ItemStack heavySword = new ItemStack(ItemInit.HEAVY_SWORD.get());
        heavySword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(List.of(handle, blade, toolBinding, handguard)));
        return heavySword;
    }

    public static ItemStack generateHeavySword(ToolMaterial material) {
        return generateHeavySword(material, material, material, material);
    }

    public static ItemStack generatePickaxe(ToolMaterial handle, ToolMaterial spikeHead, ToolMaterial toolBinding) {
        ItemStack pickaxe = new ItemStack(ItemInit.PICKAXE.get());
        pickaxe.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(List.of(handle, spikeHead, toolBinding)));
        return pickaxe;
    }

    public static ItemStack generatePickaxe(ToolMaterial material) {
        return generatePickaxe(material, material, material);
    }
}
