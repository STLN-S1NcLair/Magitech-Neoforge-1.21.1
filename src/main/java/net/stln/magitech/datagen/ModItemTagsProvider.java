package net.stln.magitech.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.data.tags.ItemTagsProvider;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.ItemTagKeys;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<net.minecraft.world.level.block.Block>> blockTags, ExistingFileHelper helper) {
        super(output, lookupProvider, blockTags, Magitech.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.LOGS).add(BlockInit.CELIFERN_LOG_ITEM.get(), BlockInit.CELIFERN_WOOD_ITEM.get(), BlockInit.STRIPPED_CELIFERN_LOG_ITEM.get(), BlockInit.STRIPPED_CELIFERN_WOOD_ITEM.get(), BlockInit.CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.CHARCOAL_BIRCH_WOOD_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM.get(), BlockInit.MYSTWOOD_LOG_ITEM.get(), BlockInit.MYSTWOOD_WOOD_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM.get());
        tag(ItemTags.LOGS_THAT_BURN).add(BlockInit.CELIFERN_LOG_ITEM.get(), BlockInit.CELIFERN_WOOD_ITEM.get(), BlockInit.STRIPPED_CELIFERN_LOG_ITEM.get(), BlockInit.STRIPPED_CELIFERN_WOOD_ITEM.get(), BlockInit.CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.CHARCOAL_BIRCH_WOOD_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM.get(), BlockInit.MYSTWOOD_LOG_ITEM.get(), BlockInit.MYSTWOOD_WOOD_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM.get());
        tag(ItemTags.PLANKS).add(BlockInit.CELIFERN_PLANKS_ITEM.get(), BlockInit.CHARCOAL_BIRCH_PLANKS_ITEM.get(), BlockInit.MYSTWOOD_PLANKS_ITEM.get());
        tag(ItemTags.LEAVES).add(BlockInit.CELIFERN_LEAVES_ITEM.get(), BlockInit.CHARCOAL_BIRCH_LEAVES_ITEM.get());
        tag(ItemTags.SAPLINGS).add(BlockInit.CELIFERN_SAPLING_ITEM.get(), BlockInit.CHARCOAL_BIRCH_SAPLING_ITEM.get());
        tag(ItemTags.FLOWERS).add(BlockInit.MISTALIA_PETALS_ITEM.get());
        tag(ItemTags.FENCE_GATES).add(BlockInit.CELIFERN_FENCE_GATE_ITEM.get(), BlockInit.CHARCOAL_BIRCH_FENCE_GATE_ITEM.get(), BlockInit.MYSTWOOD_FENCE_GATE_ITEM.get());
        tag(ItemTags.WOODEN_DOORS).add(BlockInit.CELIFERN_DOOR_ITEM.get(), BlockInit.CHARCOAL_BIRCH_DOOR_ITEM.get(), BlockInit.MYSTWOOD_DOOR_ITEM.get());
        tag(ItemTags.WOODEN_FENCES).add(BlockInit.CELIFERN_FENCE_ITEM.get(), BlockInit.CHARCOAL_BIRCH_FENCE_ITEM.get(), BlockInit.MYSTWOOD_FENCE_ITEM.get());
        tag(ItemTags.WOODEN_SLABS).add(BlockInit.CELIFERN_SLAB_ITEM.get(), BlockInit.CHARCOAL_BIRCH_SLAB_ITEM.get(), BlockInit.MYSTWOOD_SLAB_ITEM.get());
        tag(ItemTags.WOODEN_STAIRS).add(BlockInit.CELIFERN_STAIRS_ITEM.get(), BlockInit.CHARCOAL_BIRCH_STAIRS_ITEM.get(), BlockInit.MYSTWOOD_STAIRS_ITEM.get());
        tag(ItemTags.WOODEN_TRAPDOORS).add(BlockInit.CELIFERN_TRAPDOOR_ITEM.get(), BlockInit.CHARCOAL_BIRCH_TRAPDOOR_ITEM.get(), BlockInit.MYSTWOOD_TRAPDOOR_ITEM.get());
        tag(ItemTags.SLABS).add(BlockInit.ALCHECRYSITE_SLAB_ITEM.get(), BlockInit.POLISHED_ALCHECRYSITE_SLAB_ITEM.get(), BlockInit.ALCHECRYSITE_BRICK_SLAB_ITEM.get(), BlockInit.FLUORITE_BRICK_SLAB_ITEM.get());
        tag(ItemTags.STAIRS).add(BlockInit.ALCHECRYSITE_STAIRS_ITEM.get(), BlockInit.POLISHED_ALCHECRYSITE_STAIRS_ITEM.get(), BlockInit.ALCHECRYSITE_BRICK_STAIRS_ITEM.get(), BlockInit.FLUORITE_BRICK_STAIRS_ITEM.get());
        tag(ItemTags.WALLS).add(BlockInit.ALCHECRYSITE_WALL_ITEM.get(), BlockInit.POLISHED_ALCHECRYSITE_WALL_ITEM.get(), BlockInit.ALCHECRYSITE_BRICK_WALL_ITEM.get(), BlockInit.FLUORITE_BRICK_WALL_ITEM.get());
        tag(ItemTags.AXES).add(ItemInit.AXE.get());
        tag(ItemTags.PICKAXES).add(ItemInit.PICKAXE.get(), ItemInit.HAMMER.get());
        tag(ItemTags.SHOVELS).add(ItemInit.SHOVEL.get());
        tag(ItemTags.SWORDS).add(ItemInit.DAGGER.get(), ItemInit.LIGHT_SWORD.get(), ItemInit.HEAVY_SWORD.get(), ItemInit.SCYTHE.get());
        tag(ItemTagKeys.ENCHANTABLE_ARMOR).add(ItemInit.AETHER_LIFTER.get(), ItemInit.FLAMGLIDE_STRIDER.get());
        tag(ItemTagKeys.ENCHANTABLE_DURABILITY).add(ItemInit.AETHER_LIFTER.get(), ItemInit.FLAMGLIDE_STRIDER.get());
        tag(ItemTagKeys.ENCHANTABLE_EQUIPPABLE).add(ItemInit.AETHER_LIFTER.get(), ItemInit.FLAMGLIDE_STRIDER.get());
        tag(ItemTags.FOOT_ARMOR).add(ItemInit.AETHER_LIFTER.get(), ItemInit.FLAMGLIDE_STRIDER.get());
        tag(ItemTagKeys.ENCHANTABLE_FOOT_ARMOR).add(ItemInit.AETHER_LIFTER.get(), ItemInit.FLAMGLIDE_STRIDER.get());
        tag(ItemTagKeys.ENCHANTABLE_VANISHING).add(ItemInit.AETHER_LIFTER.get(), ItemInit.FLAMGLIDE_STRIDER.get());
        tag(ItemTagKeys.THREAD_BOUND).add(ItemInit.GLISTENING_LEXICON.get(), ItemInit.MATERIALS_AND_TOOLCRAFT_DESIGN.get(), ItemInit.THE_FIRE_THAT_THINKS.get(), ItemInit.APPLIED_ARCANE_CIRCUITRY.get(), ItemInit.ARCANE_ENGINEERING_COMPENDIUM.get());
        tag(ItemTagKeys.SYNTHESISED_TOOL).add(ItemInit.DAGGER.get(), ItemInit.LIGHT_SWORD.get(), ItemInit.HEAVY_SWORD.get(), ItemInit.PICKAXE.get(), ItemInit.HAMMER.get(), ItemInit.AXE.get(), ItemInit.SHOVEL.get(), ItemInit.SCYTHE.get(), ItemInit.WAND.get(), ItemInit.STAFF.get());
        tag(ItemTagKeys.REPAIR_COMPONENT).add(Items.IRON_NUGGET);
        tag(ItemTagKeys.UPGRADE_MATERIAL_0).addTag(ItemTagKeys.STRIPPED_LOGS);
        tag(ItemTagKeys.UPGRADE_MATERIAL_5).add(Items.COPPER_INGOT);
        tag(ItemTagKeys.UPGRADE_MATERIAL_10).add(Items.IRON_INGOT);
        tag(ItemTagKeys.UPGRADE_MATERIAL_15).add(Items.DIAMOND);
        tag(ItemTagKeys.UPGRADE_MATERIAL_20).add(Items.NETHERITE_SCRAP);
        tag(ItemTagKeys.CELIFERN_LOGS).add(BlockInit.CELIFERN_LOG_ITEM.get(), BlockInit.CELIFERN_WOOD_ITEM.get(), BlockInit.STRIPPED_CELIFERN_LOG_ITEM.get(), BlockInit.STRIPPED_CELIFERN_WOOD_ITEM.get());
        tag(ItemTagKeys.CHARCOAL_BIRCH_LOGS).add(BlockInit.CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.CHARCOAL_BIRCH_WOOD_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM.get());
        tag(ItemTagKeys.MYSTWOOD_LOGS).add(BlockInit.MYSTWOOD_LOG_ITEM.get(), BlockInit.MYSTWOOD_WOOD_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM.get());
        tag(ItemTagKeys.GEMS_FLUORITE).add(ItemInit.FLUORITE.get());
        tag(ItemTagKeys.GEMS_TOURMALINE).add(ItemInit.TOURMALINE.get());
        tag(ItemTagKeys.GEMS_CITRINE).add(ItemInit.CITRINE.get());
        tag(ItemTagKeys.GEMS_MANA_CHARGED_FLUORITE).add(ItemInit.MANA_CHARGED_FLUORITE.get());
        tag(ItemTagKeys.GEMS_REDSTONE_CRYSTAL).add(ItemInit.REDSTONE_CRYSTAL.get());
        tag(ItemTagKeys.GEMS_SULFUR).add(ItemInit.SULFUR.get());
        tag(ItemTagKeys.INGOTS_ZINC).add(ItemInit.ZINC_INGOT.get());
        tag(ItemTagKeys.INGOTS_FLUXIUM).add(ItemInit.FLUXIUM_INGOT.get());
        tag(ItemTagKeys.NUGGETS_FLUXIUM).add(ItemInit.FLUXIUM_NUGGET.get());
        tag(ItemTagKeys.INGOTS_ENDER_METAL).add(ItemInit.ENDER_METAL_INGOT.get());
        tag(ItemTagKeys.RAW_MATERIALS_ZINC).add(ItemInit.RAW_ZINC.get());
        tag(ItemTagKeys.ORES).add(BlockInit.FLUORITE_ORE_ITEM.get(), BlockInit.DEEPSLATE_FLUORITE_ORE_ITEM.get(), BlockInit.TOURMALINE_ORE_ITEM.get(), BlockInit.DEEPSLATE_TOURMALINE_ORE_ITEM.get(), BlockInit.ZINC_ORE_ITEM.get(), BlockInit.DEEPSLATE_ZINC_ORE_ITEM.get());
        tag(ItemTagKeys.AGGREGATED_STRAND).add(ItemInit.AGGREGATED_LUMINIS.get(), ItemInit.AGGREGATED_NOCTIS.get(), ItemInit.AGGREGATED_FLUXIA.get());
        tag(ItemTagKeys.TOOL_PART).add(ItemInit.LIGHT_BLADE.get(), ItemInit.HEAVY_BLADE.get(), ItemInit.LIGHT_HANDLE.get(), ItemInit.HEAVY_HANDLE.get(), ItemInit.TOOL_BINDING.get(), ItemInit.HANDGUARD.get(), ItemInit.STRIKE_HEAD.get(), ItemInit.SPIKE_HEAD.get(), ItemInit.REINFORCED_ROD.get(), ItemInit.PLATE.get(), ItemInit.CATALYST.get(), ItemInit.CONDUCTOR.get());
        tag(ItemTagKeys.ASPECT_CRYSTAL_BASE).addTag(ItemTagKeys.GEMS_TOURMALINE).addTag(ItemTagKeys.GEMS_QUARTZ).add(ItemInit.RESTRAINT_QUARTZ.get());
        tag(ItemTagKeys.TOOLS).addTag(ItemTagKeys.SYNTHESISED_TOOL);
        tag(ItemTagKeys.FOODS_BERRIES).add(ItemInit.MANA_BERRIES.get());
        tag(ItemTagKeys.FOODS_BERRY).add(ItemInit.MANA_BERRIES.get());
        tag(ItemTagKeys.ARMORS).add(ItemInit.AETHER_LIFTER.get(), ItemInit.FLAMGLIDE_STRIDER.get());
        tag(ItemTagKeys.ENCHANTABLES).add(ItemInit.AETHER_LIFTER.get(), ItemInit.FLAMGLIDE_STRIDER.get());
        tag(ItemTagKeys.GEMS).addTag(ItemTagKeys.GEMS_FLUORITE).addTag(ItemTagKeys.GEMS_TOURMALINE).addTag(ItemTagKeys.GEMS_MANA_CHARGED_FLUORITE).addTag(ItemTagKeys.GEMS_CITRINE).addTag(ItemTagKeys.GEMS_REDSTONE_CRYSTAL).addTag(ItemTagKeys.GEMS_SULFUR);
        tag(ItemTagKeys.INGOTS).add(ItemInit.ZINC_INGOT.get(), ItemInit.FLUXIUM_INGOT.get(), ItemInit.ENDER_METAL_INGOT.get());
        tag(ItemTagKeys.NUGGETS).add(ItemInit.FLUXIUM_NUGGET.get());
        tag(ItemTagKeys.ORES_FLUORITE).add(BlockInit.FLUORITE_ORE_ITEM.get(), BlockInit.DEEPSLATE_FLUORITE_ORE_ITEM.get());
        tag(ItemTagKeys.ORES_TOURMALINE).add(BlockInit.TOURMALINE_ORE_ITEM.get(), BlockInit.DEEPSLATE_TOURMALINE_ORE_ITEM.get());
        tag(ItemTagKeys.ORES_ZINC).add(BlockInit.ZINC_ORE_ITEM.get(), BlockInit.DEEPSLATE_ZINC_ORE_ITEM.get());
        tag(ItemTagKeys.RAW_MATERIALS).add(ItemInit.RAW_ZINC.get());
        tag(ItemTagKeys.STORAGE_BLOCKS_RAW_ZINC)
                .add(BlockInit.RAW_ZINC_BLOCK_ITEM.get());
        tag(ItemTagKeys.STORAGE_BLOCKS).add(BlockInit.RAW_ZINC_BLOCK_ITEM.get());
        tag(ItemTagKeys.STRIPPED_LOGS).add(BlockInit.STRIPPED_CELIFERN_LOG_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM.get());
        tag(ItemTagKeys.STRIPPED_WOODS).add(BlockInit.STRIPPED_CELIFERN_WOOD_ITEM.get(), BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM.get(), BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM.get());
        tag(ItemTagKeys.ORES_IN_GROUND_STONE).add(BlockInit.FLUORITE_ORE_ITEM.get(), BlockInit.TOURMALINE_ORE_ITEM.get(), BlockInit.ZINC_ORE_ITEM.get());
        tag(ItemTagKeys.ORES_IN_GROUND_DEEPSLATE).add(BlockInit.DEEPSLATE_FLUORITE_ORE_ITEM.get(), BlockInit.DEEPSLATE_TOURMALINE_ORE_ITEM.get(), BlockInit.DEEPSLATE_ZINC_ORE_ITEM.get());
    }
}
