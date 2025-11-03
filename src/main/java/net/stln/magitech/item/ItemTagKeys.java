package net.stln.magitech.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.stln.magitech.Magitech;

public class ItemTagKeys {

    public static TagKey<Item> THREAD_BOUND = TagKey.create(Registries.ITEM, Magitech.id("threadbound"));
    public static TagKey<Item> PART_TOOL = TagKey.create(Registries.ITEM, Magitech.id("part_tool"));
    public static TagKey<Item> REPAIR_COMPONENT = TagKey.create(Registries.ITEM, Magitech.id("repair_component"));
    public static TagKey<Item> UPGRADE_MATERIAL_0 = TagKey.create(Registries.ITEM, Magitech.id("upgrade_material_0"));
    public static TagKey<Item> UPGRADE_MATERIAL_5 = TagKey.create(Registries.ITEM, Magitech.id("upgrade_material_5"));
    public static TagKey<Item> UPGRADE_MATERIAL_10 = TagKey.create(Registries.ITEM, Magitech.id("upgrade_material_10"));
    public static TagKey<Item> UPGRADE_MATERIAL_15 = TagKey.create(Registries.ITEM, Magitech.id("upgrade_material_15"));
    public static TagKey<Item> UPGRADE_MATERIAL_20 = TagKey.create(Registries.ITEM, Magitech.id("upgrade_material_20"));

    public static TagKey<Item> TOOLS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "tools"));
}
