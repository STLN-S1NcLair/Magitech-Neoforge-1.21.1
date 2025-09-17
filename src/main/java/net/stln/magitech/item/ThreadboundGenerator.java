package net.stln.magitech.item;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.component.ThreadPageComponent;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.partitem.PartItem;
import net.stln.magitech.item.tool.toolitem.PartToolGenerator;
import net.stln.magitech.magic.spell.Spell;

public class ThreadboundGenerator {

    public static ItemStack generatePart(PartItem partItem, ToolMaterial material) {
        return PartToolGenerator.generatePart(partItem, material);
    }

    public static ItemStack generateThreadbound(Item item, HolderSet<Spell> holderSet) {
        ItemStack stack = new ItemStack(item);
        stack.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(holderSet, 0));
        return stack;
    }

    public static ItemStack generateThreadPage(Holder<Spell> holder) {
        ItemStack stack = ItemInit.THREAD_PAGE.toStack();
        stack.set(ComponentInit.THREAD_PAGE_COMPONENT, new ThreadPageComponent(holder));
        return stack;
    }
}
