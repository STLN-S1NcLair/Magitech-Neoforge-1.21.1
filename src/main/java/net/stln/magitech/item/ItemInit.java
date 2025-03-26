package net.stln.magitech.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.HandguardItem;
import net.stln.magitech.item.tool.LightBladeItem;
import net.stln.magitech.item.tool.LightHandleItem;
import net.stln.magitech.item.tool.LightSwordItem;

public class ItemInit {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Magitech.MOD_ID);

    public static final DeferredItem<Item> WAND = ITEMS.registerItem("wand",
            WandItem::new,
            new Item.Properties().attributes(ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "wand"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "wand"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "wand"), -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()));

    public static final DeferredItem<Item> LIGHT_BLADE = ITEMS.registerItem("light_blade",
            LightBladeItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> HANDGUARD = ITEMS.registerItem("handguard",
            HandguardItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> LIGHT_HANDLE = ITEMS.registerItem("light_handle",
            LightHandleItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> LIGHT_SWORD = ITEMS.registerItem("light_sword",
            LightSwordItem::new,
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
