package net.stln.magitech.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.Tags;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.BlockTagKeys;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
        super(output, lookupProvider, Magitech.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Blocks.ORES).add(BlockInit.FLUORITE_ORE.get(), BlockInit.DEEPSLATE_FLUORITE_ORE.get(), BlockInit.TOURMALINE_ORE.get(), BlockInit.DEEPSLATE_TOURMALINE_ORE.get(), BlockInit.ZINC_ORE.get(), BlockInit.DEEPSLATE_ZINC_ORE.get());
        tag(Tags.Blocks.STORAGE_BLOCKS).add(BlockInit.RAW_ZINC_BLOCK.get());
        tag(BlockTags.LOGS).add(BlockInit.CELIFERN_LOG.get(), BlockInit.CELIFERN_WOOD.get(), BlockInit.STRIPPED_CELIFERN_LOG.get(), BlockInit.STRIPPED_CELIFERN_WOOD.get(), BlockInit.CHARCOAL_BIRCH_LOG.get(), BlockInit.CHARCOAL_BIRCH_WOOD.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD.get(), BlockInit.MYSTWOOD_LOG.get(), BlockInit.MYSTWOOD_WOOD.get(), BlockInit.STRIPPED_MYSTWOOD_LOG.get(), BlockInit.STRIPPED_MYSTWOOD_WOOD.get());
        tag(BlockTags.LOGS_THAT_BURN).add(BlockInit.CELIFERN_LOG.get(), BlockInit.CELIFERN_WOOD.get(), BlockInit.STRIPPED_CELIFERN_LOG.get(), BlockInit.STRIPPED_CELIFERN_WOOD.get(), BlockInit.CHARCOAL_BIRCH_LOG.get(), BlockInit.CHARCOAL_BIRCH_WOOD.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD.get(), BlockInit.MYSTWOOD_LOG.get(), BlockInit.MYSTWOOD_WOOD.get(), BlockInit.STRIPPED_MYSTWOOD_LOG.get(), BlockInit.STRIPPED_MYSTWOOD_WOOD.get());
        tag(BlockTags.PLANKS).add(BlockInit.CELIFERN_PLANKS.get(), BlockInit.CHARCOAL_BIRCH_PLANKS.get(), BlockInit.MYSTWOOD_PLANKS.get());
        tag(BlockTags.LEAVES).add(BlockInit.CELIFERN_LEAVES.get(), BlockInit.CHARCOAL_BIRCH_LEAVES.get());
        tag(BlockTags.SAPLINGS).add(BlockInit.CELIFERN_SAPLING.get(), BlockInit.CHARCOAL_BIRCH_SAPLING.get());
        tag(BlockTags.FLOWERS).add(BlockInit.MISTALIA_PETALS.get());
        tag(BlockTags.DIRT).add(BlockInit.SCORCHED_GRASS_SOIL.get(), BlockInit.SCORCHED_SOIL.get());
        tag(BlockTags.FENCE_GATES).add(BlockInit.CELIFERN_FENCE_GATE.get(), BlockInit.CHARCOAL_BIRCH_FENCE_GATE.get(), BlockInit.MYSTWOOD_FENCE_GATE.get());
        tag(BlockTags.WOODEN_DOORS).add(BlockInit.CELIFERN_DOOR.get(), BlockInit.CHARCOAL_BIRCH_DOOR.get(), BlockInit.MYSTWOOD_DOOR.get());
        tag(BlockTags.WOODEN_FENCES).add(BlockInit.CELIFERN_FENCE.get(), BlockInit.CHARCOAL_BIRCH_FENCE.get(), BlockInit.MYSTWOOD_FENCE.get());
        tag(BlockTags.WOODEN_SLABS).add(BlockInit.CELIFERN_SLAB.get(), BlockInit.CHARCOAL_BIRCH_SLAB.get(), BlockInit.MYSTWOOD_SLAB.get());
        tag(BlockTags.WOODEN_STAIRS).add(BlockInit.CELIFERN_STAIRS.get(), BlockInit.CHARCOAL_BIRCH_STAIRS.get(), BlockInit.MYSTWOOD_STAIRS.get());
        tag(BlockTags.WOODEN_TRAPDOORS).add(BlockInit.CELIFERN_TRAPDOOR.get(), BlockInit.CHARCOAL_BIRCH_TRAPDOOR.get(), BlockInit.MYSTWOOD_TRAPDOOR.get());
        tag(BlockTags.SLABS).add(BlockInit.ALCHECRYSITE_SLAB.get(), BlockInit.POLISHED_ALCHECRYSITE_SLAB.get(), BlockInit.ALCHECRYSITE_BRICK_SLAB.get(), BlockInit.FLUORITE_BRICK_SLAB.get());
        tag(BlockTags.STAIRS).add(BlockInit.ALCHECRYSITE_STAIRS.get(), BlockInit.POLISHED_ALCHECRYSITE_STAIRS.get(), BlockInit.ALCHECRYSITE_BRICK_STAIRS.get(), BlockInit.FLUORITE_BRICK_STAIRS.get());
        tag(BlockTags.WALLS).add(BlockInit.ALCHECRYSITE_WALL.get(), BlockInit.POLISHED_ALCHECRYSITE_WALL.get(), BlockInit.ALCHECRYSITE_BRICK_WALL.get(), BlockInit.FLUORITE_BRICK_WALL.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(BlockInit.ENGINEERING_WORKBENCH.get(), BlockInit.ASSEMBLY_WORKBENCH.get(), BlockInit.REPAIRING_WORKBENCH.get(), BlockInit.UPGRADE_WORKBENCH.get(), BlockInit.INFUSION_ALTAR.get(), BlockInit.PEDESTAL_PYLON.get(), BlockInit.MANA_NODE.get(), BlockInit.MANA_RELAY.get(), BlockInit.MANA_VESSEL.get(), BlockInit.CELIFERN_LOG.get(), BlockInit.CELIFERN_WOOD.get(), BlockInit.STRIPPED_CELIFERN_LOG.get(), BlockInit.STRIPPED_CELIFERN_WOOD.get(), BlockInit.CELIFERN_PLANKS.get(), BlockInit.CELIFERN_SLAB.get(), BlockInit.CELIFERN_STAIRS.get(), BlockInit.CELIFERN_DOOR.get(), BlockInit.CELIFERN_TRAPDOOR.get(), BlockInit.CELIFERN_FENCE.get(), BlockInit.CELIFERN_FENCE_GATE.get(), BlockInit.CELIFERN_PRESSURE_PLATE.get(), BlockInit.CELIFERN_BUTTON.get(), BlockInit.CELIFERN_SIGN.get(), BlockInit.CELIFERN_WALL_SIGN.get(), BlockInit.CELIFERN_HANGING_SIGN.get(), BlockInit.CELIFERN_WALL_HANGING_SIGN.get(), BlockInit.CHARCOAL_BIRCH_LOG.get(), BlockInit.CHARCOAL_BIRCH_WOOD.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD.get(), BlockInit.CHARCOAL_BIRCH_PLANKS.get(), BlockInit.CHARCOAL_BIRCH_SLAB.get(), BlockInit.CHARCOAL_BIRCH_STAIRS.get(), BlockInit.CHARCOAL_BIRCH_DOOR.get(), BlockInit.CHARCOAL_BIRCH_TRAPDOOR.get(), BlockInit.CHARCOAL_BIRCH_FENCE.get(), BlockInit.CHARCOAL_BIRCH_FENCE_GATE.get(), BlockInit.CHARCOAL_BIRCH_PRESSURE_PLATE.get(), BlockInit.CHARCOAL_BIRCH_BUTTON.get(), BlockInit.CHARCOAL_BIRCH_SIGN.get(), BlockInit.CHARCOAL_BIRCH_WALL_SIGN.get(), BlockInit.CHARCOAL_BIRCH_HANGING_SIGN.get(), BlockInit.CHARCOAL_BIRCH_WALL_HANGING_SIGN.get(), BlockInit.MYSTWOOD_LOG.get(), BlockInit.MYSTWOOD_WOOD.get(), BlockInit.STRIPPED_MYSTWOOD_LOG.get(), BlockInit.STRIPPED_MYSTWOOD_WOOD.get(), BlockInit.MYSTWOOD_PLANKS.get(), BlockInit.MYSTWOOD_SLAB.get(), BlockInit.MYSTWOOD_STAIRS.get(), BlockInit.MYSTWOOD_DOOR.get(), BlockInit.MYSTWOOD_TRAPDOOR.get(), BlockInit.MYSTWOOD_FENCE.get(), BlockInit.MYSTWOOD_FENCE_GATE.get(), BlockInit.MYSTWOOD_PRESSURE_PLATE.get(), BlockInit.MYSTWOOD_BUTTON.get(), BlockInit.MYSTWOOD_SIGN.get(), BlockInit.MYSTWOOD_WALL_SIGN.get(), BlockInit.MYSTWOOD_HANGING_SIGN.get(), BlockInit.MYSTWOOD_WALL_HANGING_SIGN.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockInit.ENGINEERING_WORKBENCH.get(), BlockInit.ASSEMBLY_WORKBENCH.get(), BlockInit.REPAIRING_WORKBENCH.get(), BlockInit.UPGRADE_WORKBENCH.get(), BlockInit.INFUSION_ALTAR.get(), BlockInit.PEDESTAL_PYLON.get(), BlockInit.MANA_NODE.get(), BlockInit.MANA_RELAY.get(), BlockInit.MANA_VESSEL.get(), BlockInit.MANA_STRANDER.get(), BlockInit.MANA_RECEIVER.get(), BlockInit.MANA_COLLECTOR.get(), BlockInit.INFUSER.get(), BlockInit.ENVIROMETER.get(), BlockInit.CRUSHER.get(), BlockInit.COMPRESSOR.get(), BlockInit.ITEM_COLLECTOR.get(), BlockInit.MANA_JUNCTION.get(), BlockInit.ENTANGLER.get(), BlockInit.DETANGLER.get(), BlockInit.MANA_PUMP.get(), BlockInit.ENHANCED_MANA_NODE.get(), BlockInit.ENHANCED_MANA_RELAY.get(), BlockInit.ENHANCED_MANA_VESSEL.get(), BlockInit.CHILLER.get(), BlockInit.HEAT_BURNER.get(), BlockInit.THERMAL_MANA_FURNACE.get(), BlockInit.ZARDIUS_CRUCIBLE.get(), BlockInit.FLUORITE_ORE.get(), BlockInit.DEEPSLATE_FLUORITE_ORE.get(), BlockInit.TOURMALINE_ORE.get(), BlockInit.DEEPSLATE_TOURMALINE_ORE.get(), BlockInit.ZINC_ORE.get(), BlockInit.DEEPSLATE_ZINC_ORE.get(), BlockInit.RAW_ZINC_BLOCK.get(), BlockInit.FLUORITE_CRYSTAL_CLUSTER.get(), BlockInit.REDSTONE_CRYSTAL_CLUSTER.get(), BlockInit.SULFUR_CRYSTAL_CLUSTER.get(), BlockInit.SULFUR_BLOCK.get(), BlockInit.VESPERITE.get(), BlockInit.VESPERITE_STAIRS.get(), BlockInit.VESPERITE_SLAB.get(), BlockInit.VESPERITE_WALL.get(), BlockInit.ALCHECRYSITE.get(), BlockInit.ALCHECRYSITE_STAIRS.get(), BlockInit.ALCHECRYSITE_SLAB.get(), BlockInit.ALCHECRYSITE_WALL.get(), BlockInit.POLISHED_ALCHECRYSITE.get(), BlockInit.POLISHED_ALCHECRYSITE_STAIRS.get(), BlockInit.POLISHED_ALCHECRYSITE_SLAB.get(), BlockInit.POLISHED_ALCHECRYSITE_WALL.get(), BlockInit.ALCHECRYSITE_BRICKS.get(), BlockInit.ALCHECRYSITE_BRICK_STAIRS.get(), BlockInit.ALCHECRYSITE_BRICK_SLAB.get(), BlockInit.ALCHECRYSITE_BRICK_WALL.get(), BlockInit.ALCHECRYSITE_TILES.get(), BlockInit.FLUORITE_BLOCK.get(), BlockInit.FLUORITE_BRICKS.get(), BlockInit.FLUORITE_BRICK_STAIRS.get(), BlockInit.FLUORITE_BRICK_SLAB.get(), BlockInit.FLUORITE_BRICK_WALL.get());
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(BlockInit.SCORCHED_GRASS_SOIL.get(), BlockInit.SCORCHED_SOIL.get());
        tag(BlockTagKeys.MINABLE_WITH_SWORD).add(net.minecraft.world.level.block.Blocks.COBWEB);
        tag(BlockTagKeys.STRIPPED_LOGS).add(BlockInit.STRIPPED_CELIFERN_LOG.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG.get(), BlockInit.STRIPPED_MYSTWOOD_LOG.get());
        tag(BlockTagKeys.STRIPPED_WOODS).add(BlockInit.STRIPPED_CELIFERN_WOOD.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD.get(), BlockInit.STRIPPED_MYSTWOOD_WOOD.get());
        tag(BlockTagKeys.ORES_FLUORITE).add(BlockInit.FLUORITE_ORE.get(), BlockInit.DEEPSLATE_FLUORITE_ORE.get());
        tag(BlockTagKeys.ORES_TOURMALINE).add(BlockInit.TOURMALINE_ORE.get(), BlockInit.DEEPSLATE_TOURMALINE_ORE.get());
        tag(BlockTagKeys.ORES_ZINC).add(BlockInit.ZINC_ORE.get(), BlockInit.DEEPSLATE_ZINC_ORE.get());
        tag(BlockTagKeys.ORES_IN_GROUND_STONE).add(BlockInit.FLUORITE_ORE.get(), BlockInit.TOURMALINE_ORE.get(), BlockInit.ZINC_ORE.get());
        tag(BlockTagKeys.ORES_IN_GROUND_DEEPSLATE).add(BlockInit.DEEPSLATE_FLUORITE_ORE.get(), BlockInit.DEEPSLATE_TOURMALINE_ORE.get(), BlockInit.DEEPSLATE_ZINC_ORE.get());
        tag(BlockTagKeys.STORAGE_BLOCKS_RAW_ZINC).add(BlockInit.RAW_ZINC_BLOCK.get());
        tag(BlockTags.NEEDS_STONE_TOOL).add(BlockInit.MANA_NODE.get(), BlockInit.MANA_RELAY.get(), BlockInit.MANA_VESSEL.get(), BlockInit.MANA_STRANDER.get(), BlockInit.MANA_RECEIVER.get(), BlockInit.MANA_COLLECTOR.get(), BlockInit.INFUSER.get(), BlockInit.ENVIROMETER.get(), BlockInit.CRUSHER.get(), BlockInit.COMPRESSOR.get(), BlockInit.ITEM_COLLECTOR.get(), BlockInit.MANA_JUNCTION.get(), BlockInit.ENTANGLER.get(), BlockInit.DETANGLER.get(), BlockInit.MANA_PUMP.get(), BlockInit.ENHANCED_MANA_NODE.get(), BlockInit.ENHANCED_MANA_RELAY.get(), BlockInit.ENHANCED_MANA_VESSEL.get(), BlockInit.CHILLER.get(), BlockInit.HEAT_BURNER.get(), BlockInit.THERMAL_MANA_FURNACE.get(), BlockInit.FLUORITE_CRYSTAL_CLUSTER.get(), BlockInit.REDSTONE_CRYSTAL_CLUSTER.get(), BlockInit.FLUORITE_ORE.get(), BlockInit.DEEPSLATE_FLUORITE_ORE.get());
        tag(BlockTags.REPLACEABLE_BY_TREES).add(BlockInit.CELIFERN_LEAVES.get(), BlockInit.CHARCOAL_BIRCH_LEAVES.get());
        tag(BlockTags.SWORD_EFFICIENT).add(BlockInit.CELIFERN_LEAVES.get());
    }
}
