package net.stln.magitech.item.tool.toolitem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.MaterialComponent;
import net.stln.magitech.item.component.PartMaterialComponent;
import net.stln.magitech.item.component.ThreadPageComponent;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.partitem.PartItem;
import net.stln.magitech.magic.spell.Spell;

import java.util.List;

public class PartToolGenerator {

    public static ItemStack generatePart(PartItem partItem, ToolMaterial material) {
        ItemStack stack = new ItemStack(partItem);
        stack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(material));
        return stack;
    }

    public static ItemStack generateThreadPage(Spell spell) {
        ItemStack stack = new ItemStack(ItemInit.THREAD_PAGE.get());
        stack.set(ComponentInit.THREAD_PAGE_COMPONENT, new ThreadPageComponent(spell));
        return stack;
    }

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

    public static ItemStack generateHammer(ToolMaterial handle, ToolMaterial strikeHead, ToolMaterial plate, ToolMaterial toolBinding) {
        ItemStack heavySword = new ItemStack(ItemInit.HAMMER.get());
        heavySword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(List.of(handle, strikeHead, plate, toolBinding)));
        return heavySword;
    }

    public static ItemStack generateHammer(ToolMaterial material) {
        return generateHammer(material, material, material, material);
    }

    public static ItemStack generateScythe(ToolMaterial reinforcedStick, ToolMaterial handle, ToolMaterial blade, ToolMaterial toolBinding) {
        ItemStack scythe = new ItemStack(ItemInit.SCYTHE.get());
        scythe.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(List.of(reinforcedStick, handle, blade, toolBinding)));
        return scythe;
    }

    public static ItemStack generateScythe(ToolMaterial material) {
        return generateScythe(material, material, material, material);
    }

    public static ItemStack generateWand(ToolMaterial catalyst, ToolMaterial lightHandle, ToolMaterial conductor, ToolMaterial toolBinding) {
        ItemStack wand = new ItemStack(ItemInit.WAND.get());
        wand.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(List.of(catalyst, lightHandle, conductor, toolBinding)));
        return wand;
    }

    public static ItemStack generateWand(ToolMaterial material) {
        return generateWand(material, material, material, material);
    }
}
