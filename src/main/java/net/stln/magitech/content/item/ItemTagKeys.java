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

    public static TagKey<Item> TOOLS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "tools"));
}
