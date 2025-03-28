package net.stln.magitech.item.tool;

import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.comopnent.ComponentInit;
import net.stln.magitech.item.comopnent.MaterialComponent;
import net.stln.magitech.item.comopnent.PartMaterialComponent;

import java.util.List;

public class PartToolGenerator {

    public static ItemStack generateLightSword(ToolMaterial handle, ToolMaterial blade, ToolMaterial handguard, ToolMaterial toolBinding) {
        ItemStack lightSword = new ItemStack(ItemInit.LIGHT_SWORD.get());
        lightSword.set(ComponentInit.PART_MATERIAL_COMPONENT, new PartMaterialComponent(List.of(handle, blade, handguard, toolBinding)));
        return lightSword;
    }
}
