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
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.item.tool.toolitem.*;
import net.stln.magitech.item.tool.toolitem.WandItem;

import java.util.Map;

public class ItemInit {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Magitech.MOD_ID);

    public static final DeferredItem<Item> WAND = ITEMS.registerItem("wand",
            WandItem::new,
            new Item.Properties());

    public static final DeferredItem<ThreadboundItem> GLISTENING_LEXICON = ITEMS.registerItem("glistening_lexicon",
            (properties) -> new ThreadboundItem(properties).attributeModifier(Map.of(
                    AttributeInit.MAX_MANA, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 20, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MANA_REGEN, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 0.2, AttributeModifier.Operation.ADD_VALUE),

                    AttributeInit.NOCTIS_REGEN, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 0.2, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.LUMINIS_REGEN, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 0.2, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.FLUXIA_REGEN, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 0.2, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_NOCTIS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 10, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_LUMINIS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 10, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_FLUXIA, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 10, AttributeModifier.Operation.ADD_VALUE)
            )),
            new Item.Properties().stacksTo(1));

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

    public static final DeferredItem<Item> HEAVY_SWORD = ITEMS.registerItem("heavy_sword",
            HeavySwordItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> PICKAXE = ITEMS.registerItem("pickaxe",
            PickaxeItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> HAMMER = ITEMS.registerItem("hammer",
            HammerItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> REDSTONE_CRYSTAL = ITEMS.registerItem("redstone_crystal",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> POLISHED_REDSTONE_CRYSTAL = ITEMS.registerItem("polished_redstone_crystal",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> CHROMIUM_INGOT = ITEMS.registerItem("chromium_ingot",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> ENDER_METAL_INGOT = ITEMS.registerItem("ender_metal_ingot",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> FRIGIDITE = ITEMS.registerItem("frigidite",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> POLISHED_FRIGIDITE = ITEMS.registerItem("polished_frigidite",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> TRANSLUCIUM = ITEMS.registerItem("translucium",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> POLISHED_TRANSLUCIUM = ITEMS.registerItem("polished_translucium",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> ABYSSITE = ITEMS.registerItem("abyssite",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> POLISHED_ABYSSITE = ITEMS.registerItem("polished_abyssite",
            TooltipTextItem::new,
            new Item.Properties());

    public static void registerItems(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Items for" + Magitech.MOD_ID);
        ITEMS.register(eventBus);
    }
}
