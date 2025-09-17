package net.stln.magitech.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.Element;

public class ItemPropertyInit {
    public static void registerItemProperties() {
        ItemProperties.register(ItemInit.THREAD_PAGE.get(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "element"),
                ((stack, level, entity, seed) -> ComponentHelper.getThreadPageSpell(stack).map(Spell::getElement).map(Element::ordinal).orElse(0)));
    }
}
