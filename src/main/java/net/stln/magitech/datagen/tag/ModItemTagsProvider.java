package net.stln.magitech.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.fluid.FluidContent;
import net.stln.magitech.content.fluid.FluidInit;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.ItemTagKeys;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends HolderTagsProvider<Item> {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
        super(output, Registries.ITEM, lookupProvider, Magitech.MOD_ID, helper);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ItemTags.LOGS).add(BlockInit.CELIFERN_LOG_ITEM, BlockInit.CELIFERN_WOOD_ITEM, BlockInit.STRIPPED_CELIFERN_LOG_ITEM, BlockInit.STRIPPED_CELIFERN_WOOD_ITEM, BlockInit.CHARCOAL_BIRCH_LOG_ITEM, BlockInit.CHARCOAL_BIRCH_WOOD_ITEM, BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM, BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM, BlockInit.MYSTWOOD_LOG_ITEM, BlockInit.MYSTWOOD_WOOD_ITEM, BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM, BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM);
        tag(ItemTags.LOGS_THAT_BURN).add(BlockInit.CELIFERN_LOG_ITEM, BlockInit.CELIFERN_WOOD_ITEM, BlockInit.STRIPPED_CELIFERN_LOG_ITEM, BlockInit.STRIPPED_CELIFERN_WOOD_ITEM, BlockInit.CHARCOAL_BIRCH_LOG_ITEM, BlockInit.CHARCOAL_BIRCH_WOOD_ITEM, BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM, BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM, BlockInit.MYSTWOOD_LOG_ITEM, BlockInit.MYSTWOOD_WOOD_ITEM, BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM, BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM);
        tag(ItemTags.PLANKS).add(BlockInit.CELIFERN_PLANKS_ITEM, BlockInit.CHARCOAL_BIRCH_PLANKS_ITEM, BlockInit.MYSTWOOD_PLANKS_ITEM);
        tag(ItemTags.LEAVES).add(BlockInit.CELIFERN_LEAVES_ITEM, BlockInit.CHARCOAL_BIRCH_LEAVES_ITEM);
        tag(ItemTags.SAPLINGS).add(BlockInit.CELIFERN_SAPLING_ITEM, BlockInit.CHARCOAL_BIRCH_SAPLING_ITEM);
        tag(ItemTags.FLOWERS).add(BlockInit.MISTALIA_PETALS_ITEM);
        tag(ItemTags.FENCE_GATES).add(BlockInit.CELIFERN_FENCE_GATE_ITEM, BlockInit.CHARCOAL_BIRCH_FENCE_GATE_ITEM, BlockInit.MYSTWOOD_FENCE_GATE_ITEM);
        tag(ItemTags.WOODEN_DOORS).add(BlockInit.CELIFERN_DOOR_ITEM, BlockInit.CHARCOAL_BIRCH_DOOR_ITEM, BlockInit.MYSTWOOD_DOOR_ITEM);
        tag(ItemTags.WOODEN_FENCES).add(BlockInit.CELIFERN_FENCE_ITEM, BlockInit.CHARCOAL_BIRCH_FENCE_ITEM, BlockInit.MYSTWOOD_FENCE_ITEM);
        tag(ItemTags.WOODEN_SLABS).add(BlockInit.CELIFERN_SLAB_ITEM, BlockInit.CHARCOAL_BIRCH_SLAB_ITEM, BlockInit.MYSTWOOD_SLAB_ITEM);
        tag(ItemTags.WOODEN_STAIRS).add(BlockInit.CELIFERN_STAIRS_ITEM, BlockInit.CHARCOAL_BIRCH_STAIRS_ITEM, BlockInit.MYSTWOOD_STAIRS_ITEM);
        tag(ItemTags.WOODEN_TRAPDOORS).add(BlockInit.CELIFERN_TRAPDOOR_ITEM, BlockInit.CHARCOAL_BIRCH_TRAPDOOR_ITEM, BlockInit.MYSTWOOD_TRAPDOOR_ITEM);
        tag(ItemTags.SLABS).add(BlockInit.ALCHECRYSITE_SLAB_ITEM, BlockInit.POLISHED_ALCHECRYSITE_SLAB_ITEM, BlockInit.ALCHECRYSITE_BRICK_SLAB_ITEM, BlockInit.FLUORITE_BRICK_SLAB_ITEM);
        tag(ItemTags.STAIRS).add(BlockInit.ALCHECRYSITE_STAIRS_ITEM, BlockInit.POLISHED_ALCHECRYSITE_STAIRS_ITEM, BlockInit.ALCHECRYSITE_BRICK_STAIRS_ITEM, BlockInit.FLUORITE_BRICK_STAIRS_ITEM);
        tag(ItemTags.WALLS).add(BlockInit.ALCHECRYSITE_WALL_ITEM, BlockInit.POLISHED_ALCHECRYSITE_WALL_ITEM, BlockInit.ALCHECRYSITE_BRICK_WALL_ITEM, BlockInit.FLUORITE_BRICK_WALL_ITEM);
        tag(ItemTags.AXES).add(ItemInit.AXE);
        tag(ItemTags.PICKAXES).add(ItemInit.PICKAXE, ItemInit.HAMMER);
        tag(ItemTags.SHOVELS).add(ItemInit.SHOVEL);
        tag(ItemTags.SWORDS).add(ItemInit.DAGGER, ItemInit.LIGHT_SWORD, ItemInit.HEAVY_SWORD, ItemInit.SCYTHE);
        tag(ItemTagKeys.ENCHANTABLE_ARMOR).add(ItemInit.AETHER_LIFTER, ItemInit.FLAMGLIDE_STRIDER);
        tag(ItemTagKeys.ENCHANTABLE_DURABILITY).add(ItemInit.AETHER_LIFTER, ItemInit.FLAMGLIDE_STRIDER);
        tag(ItemTagKeys.ENCHANTABLE_EQUIPPABLE).add(ItemInit.AETHER_LIFTER, ItemInit.FLAMGLIDE_STRIDER);
        tag(ItemTags.FOOT_ARMOR).add(ItemInit.AETHER_LIFTER, ItemInit.FLAMGLIDE_STRIDER);
        tag(ItemTagKeys.ENCHANTABLE_FOOT_ARMOR).add(ItemInit.AETHER_LIFTER, ItemInit.FLAMGLIDE_STRIDER);
        tag(ItemTagKeys.ENCHANTABLE_VANISHING).add(ItemInit.AETHER_LIFTER, ItemInit.FLAMGLIDE_STRIDER);
        tag(ItemTagKeys.THREAD_BOUND).add(ItemInit.GLISTENING_LEXICON, ItemInit.MATERIALS_AND_TOOLCRAFT_DESIGN, ItemInit.THE_FIRE_THAT_THINKS, ItemInit.APPLIED_ARCANE_CIRCUITRY, ItemInit.ARCANE_ENGINEERING_COMPENDIUM);
        tag(ItemTagKeys.SYNTHESISED_TOOL).add(ItemInit.DAGGER, ItemInit.LIGHT_SWORD, ItemInit.HEAVY_SWORD, ItemInit.PICKAXE, ItemInit.HAMMER, ItemInit.AXE, ItemInit.SHOVEL, ItemInit.SCYTHE, ItemInit.WAND, ItemInit.STAFF);
        tag(ItemTagKeys.REPAIR_COMPONENT).add(Items.IRON_NUGGET.builtInRegistryHolder());
        tag(ItemTagKeys.UPGRADE_MATERIAL_0).addTag(ItemTagKeys.STRIPPED_LOGS);
        tag(ItemTagKeys.UPGRADE_MATERIAL_5).add(Items.COPPER_INGOT.builtInRegistryHolder());
        tag(ItemTagKeys.UPGRADE_MATERIAL_10).add(Items.IRON_INGOT.builtInRegistryHolder());
        tag(ItemTagKeys.UPGRADE_MATERIAL_15).add(Items.DIAMOND.builtInRegistryHolder());
        tag(ItemTagKeys.UPGRADE_MATERIAL_20).add(Items.NETHERITE_SCRAP.builtInRegistryHolder());
        tag(ItemTagKeys.CELIFERN_LOGS).add(BlockInit.CELIFERN_LOG_ITEM, BlockInit.CELIFERN_WOOD_ITEM, BlockInit.STRIPPED_CELIFERN_LOG_ITEM, BlockInit.STRIPPED_CELIFERN_WOOD_ITEM);
        tag(ItemTagKeys.CHARCOAL_BIRCH_LOGS).add(BlockInit.CHARCOAL_BIRCH_LOG_ITEM, BlockInit.CHARCOAL_BIRCH_WOOD_ITEM, BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM, BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM);
        tag(ItemTagKeys.MYSTWOOD_LOGS).add(BlockInit.MYSTWOOD_LOG_ITEM, BlockInit.MYSTWOOD_WOOD_ITEM, BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM, BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM);
        tag(ItemTagKeys.GEMS_FLUORITE).add(ItemInit.FLUORITE);
        tag(ItemTagKeys.GEMS_TOURMALINE).add(ItemInit.TOURMALINE);
        tag(ItemTagKeys.GEMS_CITRINE).add(ItemInit.CITRINE);
        tag(ItemTagKeys.GEMS_MANA_CHARGED_FLUORITE).add(ItemInit.MANA_CHARGED_FLUORITE);
        tag(ItemTagKeys.GEMS_REDSTONE_CRYSTAL).add(ItemInit.REDSTONE_CRYSTAL);
        tag(ItemTagKeys.GEMS_SULFUR).add(ItemInit.SULFUR);
        tag(ItemTagKeys.INGOTS_ZINC).add(ItemInit.ZINC_INGOT);
        tag(ItemTagKeys.INGOTS_FLUXIUM).add(ItemInit.FLUXIUM_INGOT);
        tag(ItemTagKeys.NUGGETS_FLUXIUM).add(ItemInit.FLUXIUM_NUGGET);
        tag(ItemTagKeys.INGOTS_ENDER_METAL).add(ItemInit.ENDER_METAL_INGOT);
        tag(ItemTagKeys.RAW_MATERIALS_ZINC).add(ItemInit.RAW_ZINC);
        tag(ItemTagKeys.ORES).add(BlockInit.FLUORITE_ORE_ITEM, BlockInit.DEEPSLATE_FLUORITE_ORE_ITEM, BlockInit.TOURMALINE_ORE_ITEM, BlockInit.DEEPSLATE_TOURMALINE_ORE_ITEM, BlockInit.ZINC_ORE_ITEM, BlockInit.DEEPSLATE_ZINC_ORE_ITEM);
        tag(ItemTagKeys.AGGREGATED_STRAND).add(ItemInit.AGGREGATED_LUMINIS, ItemInit.AGGREGATED_NOCTIS, ItemInit.AGGREGATED_FLUXIA);
        tag(ItemTagKeys.TOOL_PART).add(ItemInit.LIGHT_BLADE, ItemInit.HEAVY_BLADE, ItemInit.LIGHT_HANDLE, ItemInit.HEAVY_HANDLE, ItemInit.TOOL_BINDING, ItemInit.HANDGUARD, ItemInit.STRIKE_HEAD, ItemInit.SPIKE_HEAD, ItemInit.REINFORCED_ROD, ItemInit.PLATE, ItemInit.CATALYST, ItemInit.CONDUCTOR);
        tag(ItemTagKeys.ASPECT_CRYSTAL_BASE).addTag(ItemTagKeys.GEMS_TOURMALINE).addTag(ItemTagKeys.GEMS_QUARTZ).add(ItemInit.RESTRAINT_QUARTZ);
        tag(ItemTagKeys.TOOLS).addTag(ItemTagKeys.SYNTHESISED_TOOL);
        tag(ItemTagKeys.FOODS_BERRIES).add(ItemInit.MANA_BERRIES);
        tag(ItemTagKeys.FOODS_BERRY).add(ItemInit.MANA_BERRIES);
        tag(ItemTagKeys.ARMORS).add(ItemInit.AETHER_LIFTER, ItemInit.FLAMGLIDE_STRIDER);
        tag(ItemTagKeys.ENCHANTABLES).add(ItemInit.AETHER_LIFTER, ItemInit.FLAMGLIDE_STRIDER);
        tag(ItemTagKeys.GEMS).addTag(ItemTagKeys.GEMS_FLUORITE).addTag(ItemTagKeys.GEMS_TOURMALINE).addTag(ItemTagKeys.GEMS_MANA_CHARGED_FLUORITE).addTag(ItemTagKeys.GEMS_CITRINE).addTag(ItemTagKeys.GEMS_REDSTONE_CRYSTAL).addTag(ItemTagKeys.GEMS_SULFUR);
        tag(ItemTagKeys.INGOTS).add(ItemInit.ZINC_INGOT, ItemInit.FLUXIUM_INGOT, ItemInit.ENDER_METAL_INGOT);
        tag(ItemTagKeys.NUGGETS).add(ItemInit.FLUXIUM_NUGGET);
        tag(ItemTagKeys.ORES_FLUORITE).add(BlockInit.FLUORITE_ORE_ITEM, BlockInit.DEEPSLATE_FLUORITE_ORE_ITEM);
        tag(ItemTagKeys.ORES_TOURMALINE).add(BlockInit.TOURMALINE_ORE_ITEM, BlockInit.DEEPSLATE_TOURMALINE_ORE_ITEM);
        tag(ItemTagKeys.ORES_ZINC).add(BlockInit.ZINC_ORE_ITEM, BlockInit.DEEPSLATE_ZINC_ORE_ITEM);
        tag(ItemTagKeys.RAW_MATERIALS).add(ItemInit.RAW_ZINC);
        tag(ItemTagKeys.STORAGE_BLOCKS_RAW_ZINC).add(BlockInit.RAW_ZINC_BLOCK_ITEM);
        tag(ItemTagKeys.STORAGE_BLOCKS).add(BlockInit.RAW_ZINC_BLOCK_ITEM);
        tag(ItemTagKeys.STRIPPED_LOGS).add(BlockInit.STRIPPED_CELIFERN_LOG_ITEM, BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG_ITEM, BlockInit.STRIPPED_MYSTWOOD_LOG_ITEM);
        tag(ItemTagKeys.STRIPPED_WOODS).add(BlockInit.STRIPPED_CELIFERN_WOOD_ITEM, BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD_ITEM, BlockInit.STRIPPED_MYSTWOOD_WOOD_ITEM);
        tag(ItemTagKeys.ORES_IN_GROUND_STONE).add(BlockInit.FLUORITE_ORE_ITEM, BlockInit.TOURMALINE_ORE_ITEM, BlockInit.ZINC_ORE_ITEM);
        tag(ItemTagKeys.ORES_IN_GROUND_DEEPSLATE).add(BlockInit.DEEPSLATE_FLUORITE_ORE_ITEM, BlockInit.DEEPSLATE_TOURMALINE_ORE_ITEM, BlockInit.DEEPSLATE_ZINC_ORE_ITEM);
        // Fluid Buckets if present
        for (FluidContent content : FluidInit.REGISTER.getContents()) {
            content.bucketHolder().ifPresent(bucketHolder -> {
                var bucketTag = content.bucketTag();
                tag(Tags.Items.BUCKETS).addTag(bucketTag);
                tag(bucketTag).add(bucketHolder);
            });
        }
    }
}
