package net.stln.magitech.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.entity.EntityInit;
import net.stln.magitech.entity.status.AttributeInit;
import net.stln.magitech.item.armor.AetherLifterItem;
import net.stln.magitech.item.armor.FlamglideStriderItem;
import net.stln.magitech.item.component.TooltipTextPlaceableItem;
import net.stln.magitech.item.tool.partitem.*;
import net.stln.magitech.item.tool.toolitem.*;

import java.util.Map;

public class ItemInit {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Magitech.MOD_ID);

    public static final ResourceLocation THREADBOUND_ID = Magitech.id("threadbound");
    
    public static final DeferredItem<ThreadBoundItem> GLISTENING_LEXICON = ITEMS.registerItem("glistening_lexicon",
            (properties) -> new ThreadBoundItem(properties).attributeModifier(Map.of(
                    AttributeInit.MAX_MANA, new AttributeModifier(THREADBOUND_ID, 20, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MANA_REGEN, new AttributeModifier(THREADBOUND_ID, 0.2, AttributeModifier.Operation.ADD_VALUE),

                    AttributeInit.NOCTIS_REGEN, new AttributeModifier(THREADBOUND_ID, 0.2, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.LUMINIS_REGEN, new AttributeModifier(THREADBOUND_ID, 0.2, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.FLUXIA_REGEN, new AttributeModifier(THREADBOUND_ID, 0.2, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_NOCTIS, new AttributeModifier(THREADBOUND_ID, 10, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_LUMINIS, new AttributeModifier(THREADBOUND_ID, 10, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_FLUXIA, new AttributeModifier(THREADBOUND_ID, 10, AttributeModifier.Operation.ADD_VALUE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<ThreadBoundItem> THE_FIRE_THAT_THINKS = ITEMS.registerItem("the_fire_that_thinks",
            (properties) -> new ThreadBoundItem(properties).attributeModifier(Map.of(
                    AttributeInit.MAX_MANA, new AttributeModifier(THREADBOUND_ID, 100, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MANA_REGEN, new AttributeModifier(THREADBOUND_ID, 1, AttributeModifier.Operation.ADD_VALUE),

                    AttributeInit.NOCTIS_REGEN, new AttributeModifier(THREADBOUND_ID, 0.2, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.LUMINIS_REGEN, new AttributeModifier(THREADBOUND_ID, 0.2, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.FLUXIA_REGEN, new AttributeModifier(THREADBOUND_ID, 0.2, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_NOCTIS, new AttributeModifier(THREADBOUND_ID, 15, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_LUMINIS, new AttributeModifier(THREADBOUND_ID, 15, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_FLUXIA, new AttributeModifier(THREADBOUND_ID, 15, AttributeModifier.Operation.ADD_VALUE),

                    AttributeInit.SPELL_POWER, new AttributeModifier(THREADBOUND_ID, 0.2, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MANA_EFFICIENCY, new AttributeModifier(THREADBOUND_ID, -0.1, AttributeModifier.Operation.ADD_VALUE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<ThreadBoundItem> ARCANE_ENGINEERING_COMPENDIUM = ITEMS.registerItem("arcane_engineering_compendium",
            (properties) -> new ThreadBoundItem(properties).attributeModifier(Map.of(
                    AttributeInit.MAX_MANA, new AttributeModifier(THREADBOUND_ID, 300, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MANA_REGEN, new AttributeModifier(THREADBOUND_ID, 4, AttributeModifier.Operation.ADD_VALUE),

                    AttributeInit.NOCTIS_REGEN, new AttributeModifier(THREADBOUND_ID, 0.3, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.LUMINIS_REGEN, new AttributeModifier(THREADBOUND_ID, 0.3, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.FLUXIA_REGEN, new AttributeModifier(THREADBOUND_ID, 0.3, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_NOCTIS, new AttributeModifier(THREADBOUND_ID, 30, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_LUMINIS, new AttributeModifier(THREADBOUND_ID, 30, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MAX_FLUXIA, new AttributeModifier(THREADBOUND_ID, 30, AttributeModifier.Operation.ADD_VALUE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> MANA_RING = ITEMS.registerItem("mana_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.MAX_MANA, new AttributeModifier(Magitech.id("ring"), 50, AttributeModifier.Operation.ADD_VALUE),
                    AttributeInit.MANA_REGEN, new AttributeModifier(Magitech.id("ring"), 1, AttributeModifier.Operation.ADD_VALUE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> GALEVENT_RING = ITEMS.registerItem("galevent_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.COOLDOWN_SPEED, new AttributeModifier(Magitech.id("ring"), 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> CHARGEBIND_RING = ITEMS.registerItem("chargebind_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.CASTING_SPEED, new AttributeModifier(Magitech.id("ring"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> TORSION_RING = ITEMS.registerItem("torsion_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.PROJECTILE_SPEED, new AttributeModifier(Magitech.id("ring"), 0.6, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> UMBRAL_RING = ITEMS.registerItem("umbral_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.GLACE_SPELL_POWER, new AttributeModifier(Magitech.id("ring"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    AttributeInit.SURGE_SPELL_POWER, new AttributeModifier(Magitech.id("ring"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    AttributeInit.TREMOR_SPELL_POWER, new AttributeModifier(Magitech.id("ring"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    AttributeInit.HOLLOW_SPELL_POWER, new AttributeModifier(Magitech.id("ring"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    AttributeInit.MANA_EFFICIENCY, new AttributeModifier(Magitech.id("ring"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> DAWN_RING = ITEMS.registerItem("dawn_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.SURGE_SPELL_POWER, new AttributeModifier(Magitech.id("ring"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    AttributeInit.PHANTOM_SPELL_POWER, new AttributeModifier(Magitech.id("ring"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    AttributeInit.FLOW_SPELL_POWER, new AttributeModifier(Magitech.id("ring"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    AttributeInit.MANA_EFFICIENCY, new AttributeModifier(Magitech.id("ring"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<RingItem> FLUXBOUND_RING = ITEMS.registerItem("fluxbound_ring",
            (properties) -> new RingItem(properties).attributeModifier(Map.of(
                    AttributeInit.EMBER_SPELL_POWER, new AttributeModifier(Magitech.id("ring"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    AttributeInit.GLACE_SPELL_POWER, new AttributeModifier(Magitech.id("ring"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    AttributeInit.MAGIC_SPELL_POWER, new AttributeModifier(Magitech.id("ring"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                    AttributeInit.MANA_EFFICIENCY, new AttributeModifier(Magitech.id("ring"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            )),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> LIGHT_BLADE = ITEMS.registerItem("light_blade", LightBladeItem::new);

    public static final DeferredItem<Item> HEAVY_BLADE = ITEMS.registerItem("heavy_blade", HeavyBladeItem::new);

    public static final DeferredItem<Item> LIGHT_HANDLE = ITEMS.registerItem("light_handle", LightHandleItem::new);

    public static final DeferredItem<Item> HEAVY_HANDLE = ITEMS.registerItem("heavy_handle", HeavyHandleItem::new);

    public static final DeferredItem<Item> TOOL_BINDING = ITEMS.registerItem("tool_binding", ToolBindingItem::new);

    public static final DeferredItem<Item> HANDGUARD = ITEMS.registerItem("handguard", HandguardItem::new);

    public static final DeferredItem<Item> STRIKE_HEAD = ITEMS.registerItem("strike_head", StrikeHeadItem::new);

    public static final DeferredItem<Item> SPIKE_HEAD = ITEMS.registerItem("spike_head", SpikeHeadItem::new);

    public static final DeferredItem<Item> REINFORCED_STICK = ITEMS.registerItem("reinforced_stick", ReinforcedStickItem::new);

    public static final DeferredItem<Item> PLATE = ITEMS.registerItem("plate", PlateItem::new);

    public static final DeferredItem<Item> CATALYST = ITEMS.registerItem("catalyst", CatalystItem::new);

    public static final DeferredItem<Item> CONDUCTOR = ITEMS.registerItem("conductor", ConductorItem::new);

    private static final Item.Properties TOOL_PROPERTIES = new Item.Properties().setNoRepair().stacksTo(1);
    public static final DeferredItem<Item> DAGGER = ITEMS.registerItem("dagger", DaggerItem::new, TOOL_PROPERTIES);
    public static final DeferredItem<Item> LIGHT_SWORD = ITEMS.registerItem("light_sword", LightSwordItem::new, TOOL_PROPERTIES);
    public static final DeferredItem<Item> HEAVY_SWORD = ITEMS.registerItem("heavy_sword", HeavySwordItem::new, TOOL_PROPERTIES);
    public static final DeferredItem<Item> PICKAXE = ITEMS.registerItem("pickaxe", PickaxeItem::new, TOOL_PROPERTIES);
    public static final DeferredItem<Item> HAMMER = ITEMS.registerItem("hammer", HammerItem::new, TOOL_PROPERTIES);
    public static final DeferredItem<Item> AXE = ITEMS.registerItem("axe", AxeItem::new, TOOL_PROPERTIES);
    public static final DeferredItem<Item> SHOVEL = ITEMS.registerItem("shovel", ShovelItem::new, TOOL_PROPERTIES);
    public static final DeferredItem<Item> SCYTHE = ITEMS.registerItem("scythe", ScytheItem::new, TOOL_PROPERTIES);
    public static final DeferredItem<Item> WAND = ITEMS.registerItem("wand", WandItem::new, TOOL_PROPERTIES);

    public static final DeferredItem<Item> AETHER_LIFTER = ITEMS.registerItem(
            "aether_lifter",
            (properties) -> new AetherLifterItem(ArmorMaterials.IRON, ArmorItem.Type.BOOTS, properties),
            new Item.Properties().durability(314).attributes(ItemAttributeModifiers.builder().add(Attributes.SAFE_FALL_DISTANCE, new AttributeModifier(Magitech.id("aether_lifter"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET).build())
    );

    public static final DeferredItem<Item> FLAMGLIDE_STRIDER = ITEMS.registerItem(
            "flamglide_strider",
            (properties) -> new FlamglideStriderItem(ArmorMaterials.IRON, ArmorItem.Type.BOOTS, properties),
            new Item.Properties().durability(314).attributes(ItemAttributeModifiers.builder().add(Attributes.SAFE_FALL_DISTANCE, new AttributeModifier(Magitech.id("flamglide_strider"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET).build())
    );

    public static final DeferredItem<Item> THREAD_PAGE = ITEMS.registerItem("thread_page", ThreadPageItem::new, new Item.Properties().component(DataComponents.MAX_STACK_SIZE, 1));

    public static final DeferredItem<Item> ALCHAEFABRIC = ITEMS.registerItem("alchaefabric", TooltipTextItem::new);

    public static final DeferredItem<Item> AEGIS_WEAVE = ITEMS.registerItem("aegis_weave", TooltipTextItem::new);

    public static final DeferredItem<Item> FLUORITE = ITEMS.registerItem("fluorite", TooltipTextItem::new);

    public static final DeferredItem<Item> MANA_CHARGED_FLUORITE = ITEMS.registerItem("mana_charged_fluorite", ManaChargedFluoriteItem::new);

    public static final DeferredItem<Item> TOURMALINE = ITEMS.registerItem("tourmaline", TooltipTextItem::new);

    public static final DeferredItem<Item> EMBER_CRYSTAL = ITEMS.registerItem("ember_crystal", TooltipTextItem::new);

    public static final DeferredItem<Item> GLACE_CRYSTAL = ITEMS.registerItem("glace_crystal", TooltipTextItem::new);

    public static final DeferredItem<Item> SURGE_CRYSTAL = ITEMS.registerItem("surge_crystal", TooltipTextItem::new);

    public static final DeferredItem<Item> PHANTOM_CRYSTAL = ITEMS.registerItem("phantom_crystal", TooltipTextItem::new);

    public static final DeferredItem<Item> TREMOR_CRYSTAL = ITEMS.registerItem("tremor_crystal", TooltipTextItem::new);

    public static final DeferredItem<Item> MAGIC_CRYSTAL = ITEMS.registerItem("magic_crystal", TooltipTextItem::new);

    public static final DeferredItem<Item> FLOW_CRYSTAL = ITEMS.registerItem("flow_crystal", TooltipTextItem::new);

    public static final DeferredItem<Item> HOLLOW_CRYSTAL = ITEMS.registerItem("hollow_crystal", TooltipTextItem::new);

    public static final DeferredItem<Item> AGGREGATED_NOCTIS = ITEMS.registerItem("aggregated_noctis", AggregatedNoctisItem::new);

    public static final DeferredItem<Item> AGGREGATED_LUMINIS = ITEMS.registerItem("aggregated_luminis", AggregatedLuminisItem::new);

    public static final DeferredItem<Item> AGGREGATED_FLUXIA = ITEMS.registerItem("aggregated_fluxia", AggregatedFluxiaItem::new);

    public static final DeferredItem<Item> CITRINE = ITEMS.registerItem("citrine", TooltipTextItem::new);

    public static final DeferredItem<Item> REDSTONE_CRYSTAL = ITEMS.registerItem("redstone_crystal", TooltipTextItem::new);

    public static final DeferredItem<Item> POLISHED_REDSTONE_CRYSTAL = ITEMS.registerItem("polished_redstone_crystal", TooltipTextItem::new);

    public static final DeferredItem<Item> SULFUR = ITEMS.registerItem("sulfur", TooltipTextItem::new);

    public static final DeferredItem<Item> CHROMIUM_INGOT = ITEMS.registerItem("chromium_ingot", TooltipTextItem::new);

    public static final DeferredItem<Item> ENDER_METAL_INGOT = ITEMS.registerItem("ender_metal_ingot", TooltipTextItem::new);

    public static final DeferredItem<Item> NETHER_STAR_BRILLIANCE = ITEMS.registerItem("nether_star_brilliance", TooltipTextItem::new);

    public static final DeferredItem<Item> RADIANT_STEEL_INGOT = ITEMS.registerItem("radiant_steel_ingot", TooltipTextItem::new);

    public static final DeferredItem<Item> FRIGIDITE = ITEMS.registerItem("frigidite", TooltipTextItem::new);

    public static final DeferredItem<Item> POLISHED_FRIGIDITE = ITEMS.registerItem("polished_frigidite", TooltipTextItem::new);

    public static final DeferredItem<Item> TRANSLUCIUM = ITEMS.registerItem("translucium", TooltipTextItem::new);

    public static final DeferredItem<Item> POLISHED_TRANSLUCIUM = ITEMS.registerItem("polished_translucium", TooltipTextItem::new);

    public static final DeferredItem<Item> RESONITE = ITEMS.registerItem("resonite", TooltipTextItem::new);

    public static final DeferredItem<Item> POLISHED_RESONITE = ITEMS.registerItem("polished_resonite", TooltipTextItem::new);

    public static final DeferredItem<Item> ABYSSITE = ITEMS.registerItem("abyssite", TooltipTextItem::new);

    public static final DeferredItem<Item> POLISHED_ABYSSITE = ITEMS.registerItem("polished_abyssite", TooltipTextItem::new);

    public static final DeferredItem<Item> MANA_DEEXCITER_CORE = ITEMS.registerItem("mana_deexciter_core", TooltipTextItem::new);

    public static final DeferredItem<Item> ASPECT_COLLECTOR = ITEMS.registerItem("aspect_collector", TooltipTextItem::new);

    public static final DeferredItem<Item> BOOTS_FRAME = ITEMS.registerItem("boots_frame", TooltipTextItem::new);

    public static final DeferredItem<Item> MANA_BERRIES = ITEMS.registerItem("mana_berries", (properties) -> new TooltipTextPlaceableItem(BlockInit.MANA_BERRY_BUSH.get(), properties.food(FoodInit.MANA_BERRIES)));

    public static final DeferredItem<Item> MANA_PIE = ITEMS.registerItem("mana_pie", TooltipTextItem::new, new Item.Properties().food(FoodInit.MANA_PIE));

    public static final DeferredItem<Item> ALCHEMICAL_FLASK = ITEMS.registerItem("alchemical_flask", TooltipTextItem::new);

    public static final DeferredItem<Item> SULFURIC_ACID_FLASK = ITEMS.registerItem("sulfuric_acid_flask", TooltipTextItem::new);

    public static final DeferredItem<Item> WEAVER_SPAWN_EGG = ITEMS.registerItem("weaver_spawn_egg", (properties) -> new DeferredSpawnEggItem(EntityInit.WEAVER_ENTITY, 0x2F2E30, 0xB1F3CC, properties));

    public static void registerItems(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Items for" + Magitech.MOD_ID);
        ITEMS.register(eventBus);
    }
}
