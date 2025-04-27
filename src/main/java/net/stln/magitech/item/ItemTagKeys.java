package net.stln.magitech.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.stln.magitech.Magitech;

public class ItemTagKeys {

    public static TagKey<Item> THREAD_BOUND = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "threadbound"));
}
