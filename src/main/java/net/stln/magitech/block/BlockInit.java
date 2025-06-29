package net.stln.magitech.block;

import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.TooltipTextBlockItem;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.worldgen.tree.TreeGrowerInit;

public class BlockInit {

    public static final SoundType CRYSTAL = new DeferredSoundType(
            1.0F,
            1.0F,
            SoundInit.CRYSTAL_BREAK,
            SoundInit.CRYSTAL_STEP,
            SoundInit.CRYSTAL_PLACE,
            SoundInit.CRYSTAL_HIT,
            SoundInit.CRYSTAL_FALL
    );

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Magitech.MOD_ID);

    public static final DeferredBlock<EnginneringWorkbenchBlock> ENGINEERING_WORKBENCH = BLOCKS.registerBlock("engineering_workbench",
            EnginneringWorkbenchBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .strength(2F, 5.0F));

    public static final DeferredItem<BlockItem> ENGINEERING_WORKBENCH_ITEM = ItemInit.ITEMS.register("engineering_workbench", key -> new TooltipTextBlockItem(ENGINEERING_WORKBENCH.get(), new Item.Properties()));

    public static final DeferredBlock<AssemblyWorkbenchBlock> ASSEMBLY_WORKBENCH = BLOCKS.registerBlock("assembly_workbench",
            AssemblyWorkbenchBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .strength(2F, 5.0F));

    public static final DeferredItem<BlockItem> ASSEMBLY_WORKBENCH_ITEM = ItemInit.ITEMS.register("assembly_workbench", key -> new TooltipTextBlockItem(ASSEMBLY_WORKBENCH.get(), new Item.Properties()));

    public static final DeferredBlock<DropExperienceBlock> FLUORITE_ORE = BLOCKS.registerBlock("fluorite_ore",
            (properties) -> new DropExperienceBlock(UniformInt.of(1, 4),
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F)
            ));

    public static final DeferredItem<BlockItem> FLUORITE_ORE_ITEM = ItemInit.ITEMS.register("fluorite_ore", key -> new TooltipTextBlockItem(FLUORITE_ORE.get(), new Item.Properties()));

    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_FLUORITE_ORE = BLOCKS.registerBlock("deepslate_fluorite_ore",
            (properties) -> new DropExperienceBlock(UniformInt.of(1, 4),
                    BlockBehaviour.Properties.ofFullCopy(FLUORITE_ORE.get())
                            .mapColor(MapColor.DEEPSLATE)
                            .strength(4.5F, 3.0F)
                            .sound(SoundType.DEEPSLATE)
            ));

    public static final DeferredItem<BlockItem> DEEPSLATE_FLUORITE_ORE_ITEM = ItemInit.ITEMS.register("deepslate_fluorite_ore", key -> new TooltipTextBlockItem(DEEPSLATE_FLUORITE_ORE.get(), new Item.Properties()));

    public static final DeferredBlock<FluoriteCrystalClusterBlock> FLUORITE_CRYSTAL_CLUSTER = BLOCKS.registerBlock("fluorite_crystal_cluster",
            (properties) -> new FluoriteCrystalClusterBlock(UniformInt.of(0, 2), properties),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
                    .lightLevel(p_187431_ -> 8)
                    .sound(CRYSTAL)
                    .forceSolidOn()
                    .noOcclusion()
                    .strength(0.5F, 2.0F)
                    .requiresCorrectToolForDrops()
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredItem<BlockItem> FLUORITE_CRYSTAL_CLUSTER_ITEM = ItemInit.ITEMS.register("fluorite_crystal_cluster", key -> new TooltipTextBlockItem(FLUORITE_CRYSTAL_CLUSTER.get(), new Item.Properties()));

    public static final DeferredBlock<RedstoneCrystalClusterBlock> REDSTONE_CRYSTAL_CLUSTER = BLOCKS.registerBlock("redstone_crystal_cluster",
            (properties) -> new RedstoneCrystalClusterBlock(UniformInt.of(1, 3), properties),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED)
                    .lightLevel(p_187431_ -> 5)
                    .sound(CRYSTAL)
                    .forceSolidOn()
                    .noOcclusion()
                    .strength(0.5F, 2.0F)
                    .requiresCorrectToolForDrops()
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredItem<BlockItem> REDSTONE_CRYSTAL_CLUSTER_ITEM = ItemInit.ITEMS.register("redstone_crystal_cluster", key -> new TooltipTextBlockItem(REDSTONE_CRYSTAL_CLUSTER.get(), new Item.Properties()));

    public static final DeferredBlock<RotatedPillarBlock> CELIFERN_LOG = BLOCKS.register("celifern_log", key -> new RotatedPillarBlock(
            BlockBehaviour.Properties.of()
                .mapColor(p_152624_ -> p_152624_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_GREEN : MapColor.TERRACOTTA_GREEN)
            .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava()
        ));

    public static final DeferredItem<BlockItem> CELIFERN_LOG_ITEM = ItemInit.ITEMS.register("celifern_log", key -> new TooltipTextBlockItem(CELIFERN_LOG.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_PLANKS = BLOCKS.register("celifern_planks", key -> new Block(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
    ));

    public static final DeferredItem<BlockItem> CELIFERN_PLANKS_ITEM = ItemInit.ITEMS.register("celifern_planks", key -> new TooltipTextBlockItem(CELIFERN_PLANKS.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_LEAVES = BLOCKS.register("celifern_leaves", key -> new LeavesBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.AZALEA_LEAVES)
                .noOcclusion()
                .isValidSpawn(Blocks::ocelotOrParrot)
                .isSuffocating((state, blockGetter, blockPos) -> false)
                .isViewBlocking((state, blockGetter, blockPos) -> false)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor((state, blockGetter, blockPos) -> false)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_LEAVES_ITEM = ItemInit.ITEMS.register("celifern_leaves", key -> new TooltipTextBlockItem(CELIFERN_LEAVES.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_SAPLING = BLOCKS.register("celifern_sapling", key -> new SaplingBlock(
                    TreeGrowerInit.CELIFERN,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.GRASS)
                            .pushReaction(PushReaction.DESTROY)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_SAPLING_ITEM = ItemInit.ITEMS.register("celifern_sapling", key -> new TooltipTextBlockItem(CELIFERN_SAPLING.get(), new Item.Properties()));

    public static void registerBlocks(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Blocks for" + Magitech.MOD_ID);
        BLOCKS.register(eventBus);
    }
}
