package net.stln.magitech.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.tool.Element;

public class ItemPropertyInit {
    public static void registerItemProperties() {
        ItemProperties.register(ItemInit.THREAD_PAGE.get(), ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "element"),
                ((stack, level, entity, seed) -> {
                    Element element = stack.get(ComponentInit.THREAD_PAGE_COMPONENT).spell().getElement();
                    return element.ordinal();
                }));
    }
}
