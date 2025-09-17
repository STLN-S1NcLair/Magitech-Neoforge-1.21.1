package net.stln.magitech.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.item.component.ThreadPageComponent;
import net.stln.magitech.magic.spell.SpellLike;

import java.util.List;

public class ThreadboundGenerator {
    public static ItemStack generateThreadbound(Item item, List<SpellLike> holderSet) {
        ItemStack stack = new ItemStack(item);
        stack.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(holderSet));
        return stack;
    }

    public static ItemStack generateThreadPage(SpellLike holder) {
        ItemStack stack = ItemInit.THREAD_PAGE.toStack();
        stack.set(ComponentInit.THREAD_PAGE_COMPONENT, new ThreadPageComponent(holder));
        return stack;
    }
}
