package net.stln.magitech.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;

public class ItemInit {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Magitech.MOD_ID);

    public static final DeferredItem<Item> WAND = ITEMS.registerItem("wand",
            Item::new,
            new Item.Properties());

    public static final DeferredItem<Item> ENDER_METAL_INGOT = ITEMS.registerItem("ender_metal_ingot",
            Item::new,
            new Item.Properties());

    public static final DeferredItem<Item> POLISHED_FRIGIDITE = ITEMS.registerItem("polished_frigidite",
            Item::new,
            new Item.Properties());

    public static void registerItems(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Items for" + Magitech.MOD_ID);
        ITEMS.register(eventBus);
    }
}
