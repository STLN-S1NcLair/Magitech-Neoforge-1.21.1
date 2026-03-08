package net.stln.magitech.content.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.helper.ComponentHelper;

public class ItemPropertyInit {
    public static void registerItemProperties() {
        ItemProperties.register(ItemInit.THREAD_PAGE.get(), Magitech.id("element"),
                ((stack, level, entity, seed) -> ComponentHelper.getThreadPageSpell(stack).map(spell -> spell.getConfig().element()).map(Element::ordinal).orElse(0)));
    }
}
