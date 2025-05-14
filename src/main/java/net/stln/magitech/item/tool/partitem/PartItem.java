package net.stln.magitech.item.tool.partitem;


import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.tool.ToolPart;

public abstract class PartItem extends Item {
    public PartItem(Properties settings) {
        super(settings);
    }

    public abstract ToolPart getPart();

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(ComponentInit.MATERIAL_COMPONENT)) {
            return Component.translatable("item.magitech." + getPart().get(), Component.translatable("material.magitech." + stack.get(ComponentInit.MATERIAL_COMPONENT).getMaterialId()));
        }
        return super.getName(stack);
    }
}
