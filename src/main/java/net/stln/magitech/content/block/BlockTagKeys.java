package net.stln.magitech.content.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.stln.magitech.Magitech;

public class BlockTagKeys {

    public static final TagKey<Block> MINABLE_WITH_SWORD = TagKey.create(Registries.BLOCK, Magitech.id("minable_with_sword"));
    public static final TagKey<Block> STRIPPED_LOGS = external("c", "stripped_logs");
    public static final TagKey<Block> STRIPPED_WOODS = external("c", "stripped_woods");
    public static final TagKey<Block> ORES_FLUORITE = external("c", "ores/fluorite");
    public static final TagKey<Block> ORES_TOURMALINE = external("c", "ores/tourmaline");
    public static final TagKey<Block> ORES_ZINC = external("c", "ores/zinc");
    public static final TagKey<Block> ORES_IN_GROUND_STONE = external("c", "ores_in_ground/stone");
    public static final TagKey<Block> ORES_IN_GROUND_DEEPSLATE = external("c", "ores_in_ground/deepslate");
    public static final TagKey<Block> STORAGE_BLOCKS_RAW_ZINC = external("c", "storage_blocks/raw_zinc");

    private static TagKey<Block> external(String namespace, String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
