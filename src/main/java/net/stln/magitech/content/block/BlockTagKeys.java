package net.stln.magitech.content.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.Magitech;

public class BlockTagKeys {

    public static final TagKey<Block> MINABLE_WITH_SWORD = TagKey.create(Registries.BLOCK, Magitech.id("minable_with_sword"));
}
