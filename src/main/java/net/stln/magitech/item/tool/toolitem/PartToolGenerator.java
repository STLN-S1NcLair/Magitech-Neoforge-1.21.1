package net.stln.magitech.item.tool.toolitem;

import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.MaterialComponent;
import net.stln.magitech.item.component.PartMaterialComponent;
import net.stln.magitech.item.tool.material.ToolMaterialLike;
import net.stln.magitech.item.tool.partitem.PartItem;

public class PartToolGenerator {

    public static ItemStack generatePart(PartItem partItem, ToolMaterialLike material) {
        ItemStack stack = new ItemStack(partItem);
        stack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(material));
        return stack;
    }

    public static ItemStack generateDagger(ToolMaterialLike handle, ToolMaterialLike blade, ToolMaterialLike handguard) {
        ItemStack dagger = new ItemStack(ItemInit.DAGGER.get());
        dagger.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(handle, blade, handguard));
        return dagger;
    }

    public static ItemStack generateDagger(ToolMaterialLike material) {
        return generateDagger(material, material, material);
    }

    public static ItemStack generateLightSword(ToolMaterialLike handle, ToolMaterialLike blade, ToolMaterialLike handguard, ToolMaterialLike toolBinding) {
        ItemStack lightSword = new ItemStack(ItemInit.LIGHT_SWORD.get());
        lightSword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(handle, blade, handguard, toolBinding));
        return lightSword;
    }

    public static ItemStack generateLightSword(ToolMaterialLike material) {
        return generateLightSword(material, material, material, material);
    }

    public static ItemStack generateHeavySword(ToolMaterialLike handle, ToolMaterialLike blade, ToolMaterialLike toolBinding, ToolMaterialLike handguard) {
        ItemStack heavySword = new ItemStack(ItemInit.HEAVY_SWORD.get());
        heavySword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(handle, blade, toolBinding, handguard));
        return heavySword;
    }

    public static ItemStack generateHeavySword(ToolMaterialLike material) {
        return generateHeavySword(material, material, material, material);
    }

    public static ItemStack generatePickaxe(ToolMaterialLike handle, ToolMaterialLike spikeHead, ToolMaterialLike toolBinding) {
        ItemStack pickaxe = new ItemStack(ItemInit.PICKAXE.get());
        pickaxe.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(handle, spikeHead, toolBinding));
        return pickaxe;
    }

    public static ItemStack generatePickaxe(ToolMaterialLike material) {
        return generatePickaxe(material, material, material);
    }

    public static ItemStack generateHammer(ToolMaterialLike handle, ToolMaterialLike strikeHead, ToolMaterialLike plate, ToolMaterialLike toolBinding) {
        ItemStack heavySword = new ItemStack(ItemInit.HAMMER.get());
        heavySword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(handle, strikeHead, plate, toolBinding));
        return heavySword;
    }

    public static ItemStack generateHammer(ToolMaterialLike material) {
        return generateHammer(material, material, material, material);
    }

    public static ItemStack generateAxe(ToolMaterialLike handle, ToolMaterialLike blade, ToolMaterialLike strikeHead, ToolMaterialLike toolBinding) {
        ItemStack heavySword = new ItemStack(ItemInit.AXE.get());
        heavySword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(handle, blade, strikeHead, toolBinding));
        return heavySword;
    }

    public static ItemStack generateAxe(ToolMaterialLike material) {
        return generateAxe(material, material, material, material);
    }

    public static ItemStack generateShovel(ToolMaterialLike handle, ToolMaterialLike blade, ToolMaterialLike plate, ToolMaterialLike toolBinding) {
        ItemStack heavySword = new ItemStack(ItemInit.SHOVEL.get());
        heavySword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(handle, blade, plate, toolBinding));
        return heavySword;
    }

    public static ItemStack generateShovel(ToolMaterialLike material) {
        return generateShovel(material, material, material, material);
    }

    public static ItemStack generateScythe(ToolMaterialLike reinforcedStick, ToolMaterialLike handle, ToolMaterialLike blade, ToolMaterialLike toolBinding) {
        ItemStack scythe = new ItemStack(ItemInit.SCYTHE.get());
        scythe.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(reinforcedStick, handle, blade, toolBinding));
        return scythe;
    }

    public static ItemStack generateScythe(ToolMaterialLike material) {
        return generateScythe(material, material, material, material);
    }

    public static ItemStack generateWand(ToolMaterialLike catalyst, ToolMaterialLike lightHandle, ToolMaterialLike conductor, ToolMaterialLike toolBinding) {
        ItemStack wand = new ItemStack(ItemInit.WAND.get());
        wand.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(catalyst, lightHandle, conductor, toolBinding));
        return wand;
    }

    public static ItemStack generateWand(ToolMaterialLike material) {
        return generateWand(material, material, material, material);
    }
}
