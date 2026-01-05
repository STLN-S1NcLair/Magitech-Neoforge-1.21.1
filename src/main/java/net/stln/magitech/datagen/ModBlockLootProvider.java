package net.stln.magitech.datagen;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.ItemInit;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootProvider extends BlockLootSubProvider {
    protected ModBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(BlockInit.ENGINEERING_WORKBENCH.get());
        dropSelf(BlockInit.ASSEMBLY_WORKBENCH.get());
        dropSelf(BlockInit.REPAIRING_WORKBENCH.get());
        dropSelf(BlockInit.UPGRADE_WORKBENCH.get());
        dropSelf(BlockInit.TOOL_HANGER.get());
        dropSelf(BlockInit.ZARDIUS_CRUCIBLE.get());
        dropSelf(BlockInit.ALCHEMETRIC_PYLON.get());
        dropSelf(BlockInit.ATHANOR_PILLAR.get());
        dropSelf(BlockInit.MANA_NODE.get());
        dropSelf(BlockInit.MANA_RELAY.get());
        dropSelf(BlockInit.MANA_VESSEL.get());
        add(BlockInit.FLUORITE_ORE.get(),
                block -> createOreDrop(BlockInit.FLUORITE_ORE.get(), ItemInit.FLUORITE.get()));
        add(BlockInit.DEEPSLATE_FLUORITE_ORE.get(),
                block -> createOreDrop(BlockInit.DEEPSLATE_FLUORITE_ORE.get(), ItemInit.FLUORITE.get()));
        add(BlockInit.TOURMALINE_ORE.get(),
                block -> createOreDrop(BlockInit.TOURMALINE_ORE.get(), ItemInit.TOURMALINE.get()));
        add(BlockInit.DEEPSLATE_TOURMALINE_ORE.get(),
                block -> createOreDrop(BlockInit.DEEPSLATE_TOURMALINE_ORE.get(), ItemInit.TOURMALINE.get()));
        add(BlockInit.ZINC_ORE.get(),
                block -> createOreDrop(BlockInit.ZINC_ORE.get(), ItemInit.RAW_ZINC.get()));
        add(BlockInit.DEEPSLATE_ZINC_ORE.get(),
                block -> createOreDrop(BlockInit.DEEPSLATE_ZINC_ORE.get(), ItemInit.RAW_ZINC.get()));
        dropSelf(BlockInit.RAW_ZINC_BLOCK.get());
        add(BlockInit.FLUORITE_CRYSTAL_CLUSTER.get(),
                block -> createMultipleOreDrops(BlockInit.FLUORITE_CRYSTAL_CLUSTER.get(), ItemInit.FLUORITE.get(), 1, 2));
        add(BlockInit.REDSTONE_CRYSTAL_CLUSTER.get(),
                block -> createMultipleOreDrops(BlockInit.REDSTONE_CRYSTAL_CLUSTER.get(), ItemInit.REDSTONE_CRYSTAL.get(), 1, 2));
        add(BlockInit.SULFUR_CRYSTAL_CLUSTER.get(),
                block -> createMultipleOreDrops(BlockInit.SULFUR_CRYSTAL_CLUSTER.get(), ItemInit.SULFUR.get(), 1, 2));
        dropSelf(BlockInit.SULFUR_BLOCK.get());
        dropSelf(BlockInit.ALCHECRYSITE.get());
        dropSelf(BlockInit.ALCHECRYSITE_STAIRS.get());
        add(BlockInit.ALCHECRYSITE_SLAB.get(),
                block -> createSlabItemTable(BlockInit.ALCHECRYSITE_SLAB.get()));
        dropSelf(BlockInit.ALCHECRYSITE_WALL.get());
        dropSelf(BlockInit.POLISHED_ALCHECRYSITE.get());
        dropSelf(BlockInit.POLISHED_ALCHECRYSITE_STAIRS.get());
        add(BlockInit.POLISHED_ALCHECRYSITE_SLAB.get(),
                block -> createSlabItemTable(BlockInit.POLISHED_ALCHECRYSITE_SLAB.get()));
        dropSelf(BlockInit.POLISHED_ALCHECRYSITE_WALL.get());
        dropSelf(BlockInit.ALCHECRYSITE_BRICKS.get());
        dropSelf(BlockInit.ALCHECRYSITE_BRICK_STAIRS.get());
        add(BlockInit.ALCHECRYSITE_BRICK_SLAB.get(),
                block -> createSlabItemTable(BlockInit.ALCHECRYSITE_BRICK_SLAB.get()));
        dropSelf(BlockInit.ALCHECRYSITE_BRICK_WALL.get());
        dropSelf(BlockInit.ALCHECRYSITE_TILES.get());
        dropSelf(BlockInit.FLUORITE_BLOCK.get());
        dropSelf(BlockInit.FLUORITE_BRICKS.get());
        dropSelf(BlockInit.FLUORITE_BRICK_STAIRS.get());
        add(BlockInit.FLUORITE_BRICK_SLAB.get(),
                block -> createSlabItemTable(BlockInit.FLUORITE_BRICK_SLAB.get()));
        dropSelf(BlockInit.FLUORITE_BRICK_WALL.get());
        dropSelf(BlockInit.CELIFERN_LOG.get());
        dropSelf(BlockInit.CELIFERN_WOOD.get());
        dropSelf(BlockInit.STRIPPED_CELIFERN_LOG.get());
        dropSelf(BlockInit.STRIPPED_CELIFERN_WOOD.get());
        dropSelf(BlockInit.CELIFERN_PLANKS.get());
        dropSelf(BlockInit.CELIFERN_STAIRS.get());
        add(BlockInit.CELIFERN_SLAB.get(),
                block -> createSlabItemTable(BlockInit.CELIFERN_SLAB.get()));
        dropSelf(BlockInit.CELIFERN_FENCE.get());
        dropSelf(BlockInit.CELIFERN_FENCE_GATE.get());
        add(BlockInit.CELIFERN_DOOR.get(),
                block -> createDoorTable(BlockInit.CELIFERN_DOOR.get()));
        dropSelf(BlockInit.CELIFERN_TRAPDOOR.get());
        dropSelf(BlockInit.CELIFERN_PRESSURE_PLATE.get());
        dropSelf(BlockInit.CELIFERN_BUTTON.get());
        add(BlockInit.CELIFERN_LEAVES.get(),
                block -> this.createLeavesDrops(block, BlockInit.CELIFERN_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(BlockInit.CELIFERN_SAPLING.get());
        dropSelf(BlockInit.CELIFERN_SIGN.get());
        dropSelf(BlockInit.CELIFERN_WALL_SIGN.get());
        dropSelf(BlockInit.CELIFERN_HANGING_SIGN.get());
        dropSelf(BlockInit.CELIFERN_WALL_HANGING_SIGN.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_LOG.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_WOOD.get());
        dropSelf(BlockInit.STRIPPED_CHARCOAL_BIRCH_LOG.get());
        dropSelf(BlockInit.STRIPPED_CHARCOAL_BIRCH_WOOD.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_PLANKS.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_STAIRS.get());
        add(BlockInit.CHARCOAL_BIRCH_SLAB.get(),
                block -> createSlabItemTable(BlockInit.CHARCOAL_BIRCH_SLAB.get()));
        dropSelf(BlockInit.CHARCOAL_BIRCH_FENCE.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_FENCE_GATE.get());
        add(BlockInit.CHARCOAL_BIRCH_DOOR.get(),
                block -> createDoorTable(BlockInit.CHARCOAL_BIRCH_DOOR.get()));
        dropSelf(BlockInit.CHARCOAL_BIRCH_TRAPDOOR.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_PRESSURE_PLATE.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_BUTTON.get());
        add(BlockInit.CHARCOAL_BIRCH_LEAVES.get(),
                block -> this.createLeavesDrops(block, BlockInit.CHARCOAL_BIRCH_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(BlockInit.CHARCOAL_BIRCH_SAPLING.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_SIGN.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_WALL_SIGN.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_HANGING_SIGN.get());
        dropSelf(BlockInit.CHARCOAL_BIRCH_WALL_HANGING_SIGN.get());
        add(BlockInit.SCORCHED_GRASS_SOIL.get(),
                block -> createSingleItemTableWithSilkTouch(block, BlockInit.SCORCHED_SOIL));
        dropSelf(BlockInit.SCORCHED_SOIL.get());
        add(BlockInit.MISTALIA_PETALS.get(),
                block -> createPetalsDrops(BlockInit.MISTALIA_PETALS.get()));
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        this.add(BlockInit.MANA_BERRY_BUSH.get(), block -> this.applyExplosionDecay(
                block, LootTable.lootTable().withPool(LootPool.lootPool().when(
                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(BlockInit.MANA_BERRY_BUSH.get())
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 3))
                                ).add(LootItem.lootTableItem(ItemInit.MANA_BERRIES.get()))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                                .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                ).withPool(LootPool.lootPool().when(
                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(BlockInit.MANA_BERRY_BUSH.get())
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 2))
                                ).add(LootItem.lootTableItem(ItemInit.MANA_BERRIES.get()))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                )));

    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BlockInit.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
