package net.stln.magitech.content.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.stln.magitech.Magitech;

public class ItemTagKeys {

    public static final TagKey<Item> THREAD_BOUND = TagKey.create(Registries.ITEM, Magitech.id("threadbound"));
    public static final TagKey<Item> SYNTHESISED_TOOL = TagKey.create(Registries.ITEM, Magitech.id("synthesised_tool"));
    public static final TagKey<Item> REPAIR_COMPONENT = TagKey.create(Registries.ITEM, Magitech.id("repair_component"));
    public static final TagKey<Item> UPGRADE_MATERIAL_0 = TagKey.create(Registries.ITEM, Magitech.id("upgrade_material_0"));
    public static final TagKey<Item> UPGRADE_MATERIAL_5 = TagKey.create(Registries.ITEM, Magitech.id("upgrade_material_5"));
    public static final TagKey<Item> UPGRADE_MATERIAL_10 = TagKey.create(Registries.ITEM, Magitech.id("upgrade_material_10"));
    public static final TagKey<Item> UPGRADE_MATERIAL_15 = TagKey.create(Registries.ITEM, Magitech.id("upgrade_material_15"));
    public static final TagKey<Item> UPGRADE_MATERIAL_20 = TagKey.create(Registries.ITEM, Magitech.id("upgrade_material_20"));

    public static final TagKey<Item> CELIFERN_LOGS = TagKey.create(Registries.ITEM, Magitech.id("celifern_logs"));
    public static final TagKey<Item> CHARCOAL_BIRCH_LOGS = TagKey.create(Registries.ITEM, Magitech.id("charcoal_birch_logs"));
    public static final TagKey<Item> MYSTWOOD_LOGS = TagKey.create(Registries.ITEM, Magitech.id("mystwood_logs"));

    public static final TagKey<Item> ENCHANTABLE_ARMOR = external("minecraft", "enchantable/armor");
    public static final TagKey<Item> ENCHANTABLE_DURABILITY = external("minecraft", "enchantable/durability");
    public static final TagKey<Item> ENCHANTABLE_EQUIPPABLE = external("minecraft", "enchantable/equippable");
    public static final TagKey<Item> ENCHANTABLE_FOOT_ARMOR = external("minecraft", "enchantable/foot_armor");
    public static final TagKey<Item> ENCHANTABLE_VANISHING = external("minecraft", "enchantable/vanishing");
    public static final TagKey<Item> STRIPPED_LOGS = external("c", "stripped_logs");
    public static final TagKey<Item> STRIPPED_WOODS = external("c", "stripped_woods");
    public static final TagKey<Item> STONES = external("c", "stones");
    public static final TagKey<Item> ARMORS = external("c", "armors");
    public static final TagKey<Item> ENCHANTABLES = external("c", "enchantables");
    public static final TagKey<Item> FOODS_BERRIES = external("c", "foods/berries");
    public static final TagKey<Item> FOODS_BERRY = external("c", "foods/berry");
    public static final TagKey<Item> GEMS = external("c", "gems");
    public static final TagKey<Item> INGOTS_IRON = external("c", "ingots/iron");
    public static final TagKey<Item> INGOTS_COPPER = external("c", "ingots/copper");
    public static final TagKey<Item> INGOTS_GOLD = external("c", "ingots/gold");
    public static final TagKey<Item> INGOTS = external("c", "ingots");
    public static final TagKey<Item> INGOTS_ZINC = external("c", "ingots/zinc");
    public static final TagKey<Item> INGOTS_FLUXIUM = external("c", "ingots/fluxium");
    public static final TagKey<Item> INGOTS_ENDER_METAL = external("c", "ingots/ender_metal");
    public static final TagKey<Item> NUGGETS = external("c", "nuggets");
    public static final TagKey<Item> NUGGETS_FLUXIUM = external("c", "nuggets/fluxium");
    public static final TagKey<Item> GEMS_AMETHYST = external("c", "gems/amethyst");
    public static final TagKey<Item> GEMS_CITRINE = external("c", "gems/citrine");
    public static final TagKey<Item> GEMS_FLUORITE = external("c", "gems/fluorite");
    public static final TagKey<Item> GEMS_LAPIS = external("c", "gems/lapis");
    public static final TagKey<Item> GEMS_MANA_CHARGED_FLUORITE = external("c", "gems/mana_charged_fluorite");
    public static final TagKey<Item> GEMS_QUARTZ = external("c", "gems/quartz");
    public static final TagKey<Item> GEMS_REDSTONE_CRYSTAL = external("c", "gems/redstone_crystal");
    public static final TagKey<Item> GEMS_SULFUR = external("c", "gems/sulfur");
    public static final TagKey<Item> GEMS_TOURMALINE = external("c", "gems/tourmaline");
    public static final TagKey<Item> FOODS_RAW_MEAT = external("c", "foods/raw_meat");
    public static final TagKey<Item> ORES = external("c", "ores");
    public static final TagKey<Item> ORES_IN_GROUND_STONE = external("c", "ores_in_ground/stone");
    public static final TagKey<Item> ORES_IN_GROUND_DEEPSLATE = external("c", "ores_in_ground/deepslate");
    public static final TagKey<Item> RAW_MATERIALS = external("c", "raw_materials");
    public static final TagKey<Item> RAW_MATERIALS_ZINC = external("c", "raw_materials/zinc");
    public static final TagKey<Item> STORAGE_BLOCKS = external("c", "storage_blocks");
    public static final TagKey<Item> STORAGE_BLOCKS_RAW_ZINC = external("c", "storage_blocks/raw_zinc");
    public static final TagKey<Item> ORES_FLUORITE = external("c", "ores/fluorite");
    public static final TagKey<Item> ORES_TOURMALINE = external("c", "ores/tourmaline");
    public static final TagKey<Item> ORES_ZINC = external("c", "ores/zinc");
    public static final TagKey<Item> CURIOS_BELT = external("curios", "belt");
    public static final TagKey<Item> CURIOS_HEAD = external("curios", "head");
    public static final TagKey<Item> CURIOS_RING = external("curios", "ring");
    public static final TagKey<Item> AGGREGATED_STRAND = TagKey.create(Registries.ITEM, Magitech.id("aggregated_strand"));
    public static final TagKey<Item> TOOL_PART = TagKey.create(Registries.ITEM, Magitech.id("tool_part"));
    public static final TagKey<Item> ASPECT_CRYSTAL_BASE = TagKey.create(Registries.ITEM, Magitech.id("aspect_crystal_base"));
    public static final TagKey<Item> TOOLS = external("c", "tools");

    private static TagKey<Item> external(String namespace, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
