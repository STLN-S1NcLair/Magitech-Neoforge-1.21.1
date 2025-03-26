package net.stln.magitech.item.tool;

import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.comopnent.ComponentInit;
import net.stln.magitech.item.comopnent.MaterialComponent;
import net.stln.magitech.item.comopnent.PartMaterialComponent;

import java.util.List;

public class PartToolGenerator {

    public static ItemStack generateLightSword(ToolMaterial blade, ToolMaterial handguard, ToolMaterial handle) {
        ItemStack bladeItem = new ItemStack(ItemInit.LIGHT_BLADE.get());
        ItemStack handguardItem = new ItemStack(ItemInit.HANDGUARD.get());
        ItemStack handleItem = new ItemStack(ItemInit.LIGHT_HANDLE.get());
        bladeItem.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(blade));
        handguardItem.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(handguard));
        handleItem.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(handle));
        ItemStack lightSword = new ItemStack(ItemInit.LIGHT_SWORD.get());
        lightSword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(List.of(blade, handguard, handle)));
        return lightSword;
    }
}
