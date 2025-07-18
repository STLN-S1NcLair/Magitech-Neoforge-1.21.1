package net.stln.magitech.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.MaterialComponent;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.component.ThreadPageComponent;
import net.stln.magitech.item.tool.material.ToolMaterial;
import net.stln.magitech.item.tool.partitem.PartItem;
import net.stln.magitech.magic.spell.Spell;

import java.util.List;

public class ThreadboundGenerator {

    public static ItemStack generatePart(PartItem partItem, ToolMaterial material) {
        ItemStack stack = new ItemStack(partItem);
        stack.set(ComponentInit.MATERIAL_COMPONENT, new MaterialComponent(material));
        return stack;
    }

    public static ItemStack generateThreadbound(Item item, List<Spell> spell) {
        ItemStack stack = new ItemStack(item);
        stack.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spell, 0));
        return stack;
    }

    public static ItemStack generateThreadPage(Spell spell) {
        ItemStack stack = new ItemStack(ItemInit.THREAD_PAGE.get());
        stack.set(ComponentInit.THREAD_PAGE_COMPONENT, new ThreadPageComponent(spell));
        return stack;
    }
}
