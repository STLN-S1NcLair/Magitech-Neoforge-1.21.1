package net.stln.magitech.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.item.component.TooltipTextPlaceableItem;
import net.stln.magitech.item.tool.partitem.*;
import net.stln.magitech.item.tool.toolitem.*;

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

    public static final DeferredItem<ThreadboundItem> ARCANE_ENGINEERING_COMPENDIUM = ITEMS.registerItem("arcane_engineering_compendium",
            (properties) -> new ThreadboundItem(properties).attributeModifier(Map.of(
                    AttributeInit.MAX_MANA, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 300, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MANA_REGEN, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 4, AttributeModifier.Operation.ADD_VALUE),

                    AttributeInit.NOCTIS_REGEN, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 0.3, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.LUMINIS_REGEN, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 0.3, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.FLUXIA_REGEN, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 0.3, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_NOCTIS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 30, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_LUMINIS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 30, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_FLUXIA, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"), 30, AttributeModifier.Operation.ADD_VALUE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> MANA_RING = ITEMS.registerItem("mana_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.MAX_MANA, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ring"), 50, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MANA_REGEN, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ring"), 1, AttributeModifier.Operation.ADD_VALUE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> GALEVENT_RING = ITEMS.registerItem("galevent_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.COOLDOWN_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ring"), 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> CHARGEBIND_RING = ITEMS.registerItem("chargebind_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.CASTING_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ring"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> TORSION_RING = ITEMS.registerItem("torsion_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.PROJECTILE_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "ring"), 0.6, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> LIGHT_BLADE = ITEMS.registerItem("light_blade",
            LightBladeItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> HEAVY_BLADE = ITEMS.registerItem("heavy_blade",
            HeavyBladeItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> LIGHT_HANDLE = ITEMS.registerItem("light_handle",
            LightHandleItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> HEAVY_HANDLE = ITEMS.registerItem("heavy_handle",
            HeavyHandleItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> TOOL_BINDING = ITEMS.registerItem("tool_binding",
            ToolBindingItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> HANDGUARD = ITEMS.registerItem("handguard",
            HandguardItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> STRIKE_HEAD = ITEMS.registerItem("strike_head",
            StrikeHeadItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> SPIKE_HEAD = ITEMS.registerItem("spike_head",
            SpikeHeadItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> REINFORCED_STICK = ITEMS.registerItem("reinforced_stick",
            ReinforcedStickItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> PLATE = ITEMS.registerItem("plate",
            PlateItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> CATALYST = ITEMS.registerItem("catalyst",
            CatalystItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> CONDUCTOR = ITEMS.registerItem("conductor",
            ConductorItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> DAGGER = ITEMS.registerItem("dagger",
            DaggerItem::new,
            new Item.Properties().setNoRepair().stacksTo(1));

    public static final DeferredItem<Item> LIGHT_SWORD = ITEMS.registerItem("light_sword",
            LightSwordItem::new,
            new Item.Properties().setNoRepair().stacksTo(1));

    public static final DeferredItem<Item> HEAVY_SWORD = ITEMS.registerItem("heavy_sword",
            HeavySwordItem::new,
            new Item.Properties().setNoRepair().stacksTo(1));

    public static final DeferredItem<Item> PICKAXE = ITEMS.registerItem("pickaxe",
            PickaxeItem::new,
            new Item.Properties().setNoRepair().stacksTo(1));

    public static final DeferredItem<Item> HAMMER = ITEMS.registerItem("hammer",
            HammerItem::new,
            new Item.Properties().setNoRepair().stacksTo(1));

    public static final DeferredItem<Item> AXE = ITEMS.registerItem("axe",
            AxeItem::new,
            new Item.Properties().setNoRepair().stacksTo(1));

    public static final DeferredItem<Item> SHOVEL = ITEMS.registerItem("shovel",
            ShovelItem::new,
            new Item.Properties().setNoRepair().stacksTo(1));

    public static final DeferredItem<Item> SCYTHE = ITEMS.registerItem("scythe",
            ScytheItem::new,
            new Item.Properties().setNoRepair().stacksTo(1));

    public static final DeferredItem<Item> THREAD_PAGE = ITEMS.registerItem("thread_page",
            ThreadPageItem::new,
            new Item.Properties().component(DataComponents.MAX_STACK_SIZE, 1));

    public static final DeferredItem<Item> FLUORITE = ITEMS.registerItem("fluorite",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> MANA_CHARGED_FLUORITE = ITEMS.registerItem("mana_charged_fluorite",
            ManaChargedFluoriteItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> CITRINE = ITEMS.registerItem("citrine",
            TooltipTextItem::new,
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

    public static final DeferredItem<Item> RESONITE = ITEMS.registerItem("resonite",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> POLISHED_RESONITE = ITEMS.registerItem("polished_resonite",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> ABYSSITE = ITEMS.registerItem("abyssite",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> POLISHED_ABYSSITE = ITEMS.registerItem("polished_abyssite",
            TooltipTextItem::new,
            new Item.Properties());

    public static final DeferredItem<Item> MANA_BERRIES = ITEMS.registerItem("mana_berries",
            (key) -> new TooltipTextPlaceableItem(BlockInit.MANA_BERRY_BUSH.get(),
            new Item.Properties().food(FoodInit.MANA_BERRIES)));

    public static void registerItems(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Items for" + Magitech.MOD_ID);
        ITEMS.register(eventBus);
    }
}
