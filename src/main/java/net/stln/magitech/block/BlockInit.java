package net.stln.magitech.block;

import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.block_entity.AlchemetricPylonBlockEntity;
import net.stln.magitech.block.block_entity.AthanorPillarBlockEntity;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import net.stln.magitech.block.block_entity.ZardiusCrucibleBlockEntity;
import net.stln.magitech.block.block_entity.renderer.ManaVesselBlockEntityRenderer;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.TooltipTextBlockItem;
import net.stln.magitech.item.TooltipTextSignItem;
import net.stln.magitech.sound.SoundInit;
import net.stln.magitech.worldgen.tree.TreeGrowerInit;

import java.util.function.Supplier;

public class BlockInit {

    public static final SoundType CRYSTAL_SOUND = new DeferredSoundType(
            1.0F,
            1.0F,
            SoundInit.CRYSTAL_BREAK,
            SoundInit.CRYSTAL_STEP,
            SoundInit.CRYSTAL_PLACE,
            SoundInit.CRYSTAL_HIT,
            SoundInit.CRYSTAL_FALL
    );

    public static final SoundType ALCHECRYSITE_SOUND = new DeferredSoundType(
            0.5F,
            1.0F,
            SoundInit.ALCHECRYSITE_BREAK,
            SoundInit.ALCHECRYSITE_STEP,
            SoundInit.ALCHECRYSITE_PLACE,
            SoundInit.ALCHECRYSITE_HIT,
            SoundInit.ALCHECRYSITE_FALL
    );

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Magitech.MOD_ID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Magitech.MOD_ID);

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

    public static final DeferredBlock<AlchemetricPylonBlock> ALCHEMETRIC_PYLON = BLOCKS.registerBlock("alchemetric_pylon",
            AlchemetricPylonBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).sound(ALCHECRYSITE_SOUND));

    public static final Supplier<BlockEntityType<AlchemetricPylonBlockEntity>> ALCHEMETRIC_PYLON_ENTITY =
            BLOCK_ENITIES.register("alchemetric_pylon", () -> BlockEntityType.Builder.of(
                    AlchemetricPylonBlockEntity::new, BlockInit.ALCHEMETRIC_PYLON.get()).build(null));

    public static final DeferredItem<BlockItem> ALCHEMETRIC_PYLON_ITEM = ItemInit.ITEMS.register("alchemetric_pylon", key -> new TooltipTextBlockItem(ALCHEMETRIC_PYLON.get(), new Item.Properties()));

    public static final DeferredBlock<AthanorPillarBlock> ATHANOR_PILLAR = BLOCKS.registerBlock("athanor_pillar",
            AthanorPillarBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).sound(ALCHECRYSITE_SOUND));

    public static final Supplier<BlockEntityType<AthanorPillarBlockEntity>> ATHANOR_PILLAR_ENTITY =
            BLOCK_ENITIES.register("athanor_pillar", () -> BlockEntityType.Builder.of(
                    AthanorPillarBlockEntity::new, BlockInit.ATHANOR_PILLAR.get()).build(null));

    public static final DeferredItem<BlockItem> ATHANOR_PILLAR_ITEM = ItemInit.ITEMS.register("athanor_pillar", key -> new TooltipTextBlockItem(ATHANOR_PILLAR.get(), new Item.Properties()));

    public static final DeferredBlock<ManaNodeBlock> MANA_NODE = BLOCKS.registerBlock("mana_node",
            ManaNodeBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).sound(CRYSTAL_SOUND).lightLevel((blockState) -> 10));

    public static final DeferredItem<BlockItem> MANA_NODE_ITEM = ItemInit.ITEMS.register("mana_node", key -> new TooltipTextBlockItem(MANA_NODE.get(), new Item.Properties()));

    public static final DeferredBlock<ZardiusCrucibleBlock> ZARDIUS_CRUCIBLE = BLOCKS.registerBlock("zardius_crucible",
            ZardiusCrucibleBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).sound(SoundType.NETHERITE_BLOCK).lightLevel((blockState) -> 5).noOcclusion());

    public static final Supplier<BlockEntityType<ZardiusCrucibleBlockEntity>> ZARDIUS_CRUCIBLE_ENTITY =
            BLOCK_ENITIES.register("zardius_crucible", () -> BlockEntityType.Builder.of(
                    ZardiusCrucibleBlockEntity::new, BlockInit.ZARDIUS_CRUCIBLE.get()).build(null));

    public static final DeferredItem<BlockItem> ZARDIUS_CRUCIBLE_ITEM = ItemInit.ITEMS.register("zardius_crucible", key -> new TooltipTextBlockItem(ZARDIUS_CRUCIBLE.get(), new Item.Properties()));

    public static final DeferredBlock<ManaVesselBlock> MANA_VESSEL = BLOCKS.registerBlock("mana_vessel",
            ManaVesselBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).sound(SoundType.NETHERITE_BLOCK).lightLevel((blockState) -> 5).noOcclusion());

    public static final Supplier<BlockEntityType<ManaVesselBlockEntity>> MANA_VESSEL_ENTITY =
            BLOCK_ENITIES.register("mana_vessel", () -> BlockEntityType.Builder.of(
                    ManaVesselBlockEntity::new, BlockInit.MANA_VESSEL.get()).build(null));

    public static final DeferredItem<BlockItem> MANA_VESSEL_ITEM = ItemInit.ITEMS.register("mana_vessel", key -> new TooltipTextBlockItem(MANA_VESSEL.get(), new Item.Properties()));

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

    public static final DeferredBlock<DropExperienceBlock> TOURMALINE_ORE = BLOCKS.registerBlock("tourmaline_ore",
            (properties) -> new DropExperienceBlock(UniformInt.of(1, 4),
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F)
            ));

    public static final DeferredItem<BlockItem> TOURMALINE_ORE_ITEM = ItemInit.ITEMS.register("tourmaline_ore", key -> new TooltipTextBlockItem(TOURMALINE_ORE.get(), new Item.Properties()));

    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_TOURMALINE_ORE = BLOCKS.registerBlock("deepslate_tourmaline_ore",
            (properties) -> new DropExperienceBlock(UniformInt.of(1, 4),
                    BlockBehaviour.Properties.ofFullCopy(TOURMALINE_ORE.get())
                            .mapColor(MapColor.DEEPSLATE)
                            .strength(4.5F, 3.0F)
                            .sound(SoundType.DEEPSLATE)
            ));

    public static final DeferredItem<BlockItem> DEEPSLATE_TOURMALINE_ORE_ITEM = ItemInit.ITEMS.register("deepslate_tourmaline_ore", key -> new TooltipTextBlockItem(DEEPSLATE_TOURMALINE_ORE.get(), new Item.Properties()));

    public static final DeferredBlock<FluoriteCrystalClusterBlock> FLUORITE_CRYSTAL_CLUSTER = BLOCKS.registerBlock("fluorite_crystal_cluster",
            (properties) -> new FluoriteCrystalClusterBlock(UniformInt.of(0, 2), properties),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
                    .lightLevel(p_187431_ -> 8)
                    .sound(CRYSTAL_SOUND)
                    .forceSolidOn()
                    .noOcclusion()
                    .strength(0.5F, 2.0F)
                    .requiresCorrectToolForDrops()
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredItem<BlockItem> FLUORITE_CRYSTAL_CLUSTER_ITEM = ItemInit.ITEMS.register("fluorite_crystal_cluster", key -> new TooltipTextBlockItem(FLUORITE_CRYSTAL_CLUSTER.get(), new Item.Properties()));

    public static final DeferredBlock<RedstoneCrystalClusterBlock> REDSTONE_CRYSTAL_CLUSTER = BLOCKS.registerBlock("redstone_crystal_cluster",
            (properties) -> new RedstoneCrystalClusterBlock(UniformInt.of(1, 3), properties),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .lightLevel(p_187431_ -> 5)
                    .sound(CRYSTAL_SOUND)
                    .forceSolidOn()
                    .noOcclusion()
                    .strength(0.5F, 2.0F)
                    .requiresCorrectToolForDrops()
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredItem<BlockItem> REDSTONE_CRYSTAL_CLUSTER_ITEM = ItemInit.ITEMS.register("redstone_crystal_cluster", key -> new TooltipTextBlockItem(REDSTONE_CRYSTAL_CLUSTER.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ALCHECRYSITE = BLOCKS.registerBlock("alchecrysite",
            (properties) -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> ALCHECRYSITE_ITEM = ItemInit.ITEMS.register("alchecrysite", key -> new TooltipTextBlockItem(ALCHECRYSITE.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ALCHECRYSITE_STAIRS = BLOCKS.registerBlock("alchecrysite_stairs",
            (properties) -> new StairBlock(
                    ALCHECRYSITE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_STAIRS).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> ALCHECRYSITE_STAIRS_ITEM = ItemInit.ITEMS.register("alchecrysite_stairs", key -> new TooltipTextBlockItem(ALCHECRYSITE_STAIRS.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ALCHECRYSITE_SLAB = BLOCKS.registerBlock("alchecrysite_slab",
            (properties) -> new SlabBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_SLAB).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> ALCHECRYSITE_SLAB_ITEM = ItemInit.ITEMS.register("alchecrysite_slab", key -> new TooltipTextBlockItem(ALCHECRYSITE_SLAB.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ALCHECRYSITE_WALL = BLOCKS.registerBlock("alchecrysite_wall",
            (properties) -> new WallBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_WALL).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> ALCHECRYSITE_WALL_ITEM = ItemInit.ITEMS.register("alchecrysite_wall", key -> new TooltipTextBlockItem(ALCHECRYSITE_WALL.get(), new Item.Properties()));

    public static final DeferredBlock<Block> POLISHED_ALCHECRYSITE = BLOCKS.registerBlock("polished_alchecrysite",
            (properties) -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> POLISHED_ALCHECRYSITE_ITEM = ItemInit.ITEMS.register("polished_alchecrysite", key -> new TooltipTextBlockItem(POLISHED_ALCHECRYSITE.get(), new Item.Properties()));

    public static final DeferredBlock<Block> POLISHED_ALCHECRYSITE_STAIRS = BLOCKS.registerBlock("polished_alchecrysite_stairs",
            (properties) -> new StairBlock(
                    POLISHED_ALCHECRYSITE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_STAIRS).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> POLISHED_ALCHECRYSITE_STAIRS_ITEM = ItemInit.ITEMS.register("polished_alchecrysite_stairs", key -> new TooltipTextBlockItem(POLISHED_ALCHECRYSITE_STAIRS.get(), new Item.Properties()));

    public static final DeferredBlock<Block> POLISHED_ALCHECRYSITE_SLAB = BLOCKS.registerBlock("polished_alchecrysite_slab",
            (properties) -> new SlabBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_SLAB).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> POLISHED_ALCHECRYSITE_SLAB_ITEM = ItemInit.ITEMS.register("polished_alchecrysite_slab", key -> new TooltipTextBlockItem(POLISHED_ALCHECRYSITE_SLAB.get(), new Item.Properties()));

    public static final DeferredBlock<Block> POLISHED_ALCHECRYSITE_WALL = BLOCKS.registerBlock("polished_alchecrysite_wall",
            (properties) -> new WallBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_WALL).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> POLISHED_ALCHECRYSITE_WALL_ITEM = ItemInit.ITEMS.register("polished_alchecrysite_wall", key -> new TooltipTextBlockItem(POLISHED_ALCHECRYSITE_WALL.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ALCHECRYSITE_BRICKS = BLOCKS.registerBlock("alchecrysite_bricks",
            (properties) -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> ALCHECRYSITE_BRICKS_ITEM = ItemInit.ITEMS.register("alchecrysite_bricks", key -> new TooltipTextBlockItem(ALCHECRYSITE_BRICKS.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ALCHECRYSITE_BRICK_STAIRS = BLOCKS.registerBlock("alchecrysite_brick_stairs",
            (properties) -> new StairBlock(
                    ALCHECRYSITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_STAIRS).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> ALCHECRYSITE_BRICK_STAIRS_ITEM = ItemInit.ITEMS.register("alchecrysite_brick_stairs", key -> new TooltipTextBlockItem(ALCHECRYSITE_BRICK_STAIRS.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ALCHECRYSITE_BRICK_SLAB = BLOCKS.registerBlock("alchecrysite_brick_slab",
            (properties) -> new SlabBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_SLAB).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> ALCHECRYSITE_BRICK_SLAB_ITEM = ItemInit.ITEMS.register("alchecrysite_brick_slab", key -> new TooltipTextBlockItem(ALCHECRYSITE_BRICK_SLAB.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ALCHECRYSITE_BRICK_WALL = BLOCKS.registerBlock("alchecrysite_brick_wall",
            (properties) -> new WallBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_WALL).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> ALCHECRYSITE_BRICK_WALL_ITEM = ItemInit.ITEMS.register("alchecrysite_brick_wall", key -> new TooltipTextBlockItem(ALCHECRYSITE_BRICK_WALL.get(), new Item.Properties()));

    public static final DeferredBlock<Block> ALCHECRYSITE_TILES = BLOCKS.registerBlock("alchecrysite_tiles",
            (properties) -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_TILES).sound(ALCHECRYSITE_SOUND)
            ));

    public static final DeferredItem<BlockItem> ALCHECRYSITE_TILES_ITEM = ItemInit.ITEMS.register("alchecrysite_tiles", key -> new TooltipTextBlockItem(ALCHECRYSITE_TILES.get(), new Item.Properties()));

    public static final DeferredBlock<Block> FLUORITE_BLOCK = BLOCKS.registerBlock("fluorite_block",
            (properties) -> new Block(
                    BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
                            .sound(CRYSTAL_SOUND)
                            .requiresCorrectToolForDrops()
                            .strength(1.5F, 2.0F)
            ));

    public static final DeferredItem<BlockItem> FLUORITE_BLOCK_ITEM = ItemInit.ITEMS.register("fluorite_block", key -> new TooltipTextBlockItem(FLUORITE_BLOCK.get(), new Item.Properties()));

    public static final DeferredBlock<Block> FLUORITE_BRICKS = BLOCKS.registerBlock("fluorite_bricks",
            (properties) -> new Block(
                    BlockBehaviour.Properties.ofFullCopy(BlockInit.FLUORITE_BLOCK.get())
            ));

    public static final BlockSetType FLUORITE_BRICK_SET_TYPE = BlockSetType.register(new BlockSetType("magitech:fluorite_brick"));

    public static final DeferredItem<BlockItem> FLUORITE_BRICKS_ITEM = ItemInit.ITEMS.register("fluorite_bricks", key -> new TooltipTextBlockItem(FLUORITE_BRICKS.get(), new Item.Properties()));

    public static final DeferredBlock<Block> FLUORITE_BRICK_STAIRS = BLOCKS.register("fluorite_brick_stairs", key -> new StairBlock(
            FLUORITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_STAIRS).sound(CRYSTAL_SOUND)
    ));

    public static final DeferredItem<BlockItem> FLUORITE_BRICK_STAIRS_ITEM = ItemInit.ITEMS.register("fluorite_brick_stairs", key -> new TooltipTextBlockItem(FLUORITE_BRICK_STAIRS.get(), new Item.Properties()));

    public static final DeferredBlock<Block> FLUORITE_BRICK_SLAB = BLOCKS.register("fluorite_brick_slab", key -> new SlabBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_STAIRS).sound(CRYSTAL_SOUND)
    ));

    public static final DeferredItem<BlockItem> FLUORITE_BRICK_SLAB_ITEM = ItemInit.ITEMS.register("fluorite_brick_slab", key -> new TooltipTextBlockItem(FLUORITE_BRICK_SLAB.get(), new Item.Properties()));

    public static final DeferredBlock<Block> FLUORITE_BRICK_WALL = BLOCKS.register("fluorite_brick_wall", key -> new WallBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_WALL).sound(CRYSTAL_SOUND)
    ));

    public static final DeferredItem<BlockItem> FLUORITE_BRICK_WALL_ITEM = ItemInit.ITEMS.register("fluorite_brick_wall", key -> new TooltipTextBlockItem(FLUORITE_BRICK_WALL.get(), new Item.Properties()));

    public static final BlockSetType CELIFERN_SET_TYPE = BlockSetType.register(new BlockSetType("magitech:celifern"));
    public static final WoodType CELIFERN_WOOD_TYPE = WoodType.register(new WoodType("magitech:celifern", CELIFERN_SET_TYPE));

    public static final DeferredBlock<RotatedPillarBlock> CELIFERN_LOG = BLOCKS.register("celifern_log", key -> new RotatedPillarBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                .mapColor(p_152624_ -> p_152624_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_GREEN : MapColor.TERRACOTTA_GREEN)
        ));

    public static final DeferredItem<BlockItem> CELIFERN_LOG_ITEM = ItemInit.ITEMS.register("celifern_log", key -> new TooltipTextBlockItem(CELIFERN_LOG.get(), new Item.Properties()));

    public static final DeferredBlock<RotatedPillarBlock> CELIFERN_WOOD = BLOCKS.register("celifern_wood", key -> new RotatedPillarBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)
                .mapColor(MapColor.TERRACOTTA_GREEN)
        ));

    public static final DeferredItem<BlockItem> CELIFERN_WOOD_ITEM = ItemInit.ITEMS.register("celifern_wood", key -> new TooltipTextBlockItem(CELIFERN_WOOD.get(), new Item.Properties()));

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CELIFERN_LOG = BLOCKS.register("stripped_celifern_log", key -> new RotatedPillarBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)
                .mapColor(MapColor.COLOR_GREEN)
        ));

    public static final DeferredItem<BlockItem> STRIPPED_CELIFERN_LOG_ITEM = ItemInit.ITEMS.register("stripped_celifern_log", key -> new TooltipTextBlockItem(STRIPPED_CELIFERN_LOG.get(), new Item.Properties()));

    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_CELIFERN_WOOD = BLOCKS.register("stripped_celifern_wood", key -> new RotatedPillarBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)
                    .mapColor(MapColor.COLOR_GREEN)
        ));

    public static final DeferredItem<BlockItem> STRIPPED_CELIFERN_WOOD_ITEM = ItemInit.ITEMS.register("stripped_celifern_wood", key -> new TooltipTextBlockItem(STRIPPED_CELIFERN_WOOD.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_PLANKS = BLOCKS.register("celifern_planks", key -> new Block(
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_PLANKS_ITEM = ItemInit.ITEMS.register("celifern_planks", key -> new TooltipTextBlockItem(CELIFERN_PLANKS.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_SLAB = BLOCKS.register("celifern_slab", key -> new SlabBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_SLAB_ITEM = ItemInit.ITEMS.register("celifern_slab", key -> new TooltipTextBlockItem(CELIFERN_SLAB.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_STAIRS = BLOCKS.register("celifern_stairs", key -> new StairBlock(
            CELIFERN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_STAIRS_ITEM = ItemInit.ITEMS.register("celifern_stairs", key -> new TooltipTextBlockItem(CELIFERN_STAIRS.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_DOOR = BLOCKS.register("celifern_door", key -> new DoorBlock(
            CELIFERN_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_DOOR_ITEM = ItemInit.ITEMS.register("celifern_door", key -> new TooltipTextBlockItem(CELIFERN_DOOR.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_TRAPDOOR = BLOCKS.register("celifern_trapdoor", key -> new TrapDoorBlock(
            CELIFERN_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_TRAPDOOR_ITEM = ItemInit.ITEMS.register("celifern_trapdoor", key -> new TooltipTextBlockItem(CELIFERN_TRAPDOOR.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_FENCE = BLOCKS.register("celifern_fence", key -> new FenceBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_FENCE_ITEM = ItemInit.ITEMS.register("celifern_fence", key -> new TooltipTextBlockItem(CELIFERN_FENCE.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_FENCE_GATE = BLOCKS.register("celifern_fence_gate", key -> new FenceGateBlock(
            CELIFERN_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_FENCE_GATE_ITEM = ItemInit.ITEMS.register("celifern_fence_gate", key -> new TooltipTextBlockItem(CELIFERN_FENCE_GATE.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_PRESSURE_PLATE = BLOCKS.register("celifern_pressure_plate", key -> new PressurePlateBlock(
            CELIFERN_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_PRESSURE_PLATE_ITEM = ItemInit.ITEMS.register("celifern_pressure_plate", key -> new TooltipTextBlockItem(CELIFERN_PRESSURE_PLATE.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_BUTTON = BLOCKS.register("celifern_button", key -> new ButtonBlock(
            CELIFERN_SET_TYPE, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_BUTTON_ITEM = ItemInit.ITEMS.register("celifern_button", key -> new TooltipTextBlockItem(CELIFERN_BUTTON.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_SIGN = BLOCKS.register("celifern_sign", key -> new StandingSignBlock(
            CELIFERN_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)
    ));

    public static final DeferredBlock<Block> CELIFERN_WALL_SIGN = BLOCKS.register("celifern_wall_sign", key -> new WallSignBlock(
            CELIFERN_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_SIGN_ITEM = ItemInit.ITEMS.register("celifern_sign", key -> new TooltipTextSignItem(new Item.Properties(), CELIFERN_SIGN.get(), CELIFERN_WALL_SIGN.get()));

    public static final DeferredBlock<Block> CELIFERN_HANGING_SIGN = BLOCKS.register("celifern_hanging_sign", key -> new CeilingHangingSignBlock(
            CELIFERN_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN)
    ));

    public static final DeferredBlock<Block> CELIFERN_WALL_HANGING_SIGN = BLOCKS.register("celifern_wall_hanging_sign", key -> new WallHangingSignBlock(
            CELIFERN_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_HANGING_SIGN_ITEM = ItemInit.ITEMS.register("celifern_hanging_sign", key -> new TooltipTextSignItem(new Item.Properties(), CELIFERN_HANGING_SIGN.get(), CELIFERN_WALL_HANGING_SIGN.get()));

    public static final DeferredBlock<Block> CELIFERN_LEAVES = BLOCKS.register("celifern_leaves", key -> new LeavesBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.AZALEA_LEAVES)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_LEAVES_ITEM = ItemInit.ITEMS.register("celifern_leaves", key -> new TooltipTextBlockItem(CELIFERN_LEAVES.get(), new Item.Properties()));

    public static final DeferredBlock<Block> CELIFERN_SAPLING = BLOCKS.register("celifern_sapling", key -> new SaplingBlock(
                    TreeGrowerInit.CELIFERN,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)
    ));

    public static final DeferredItem<BlockItem> CELIFERN_SAPLING_ITEM = ItemInit.ITEMS.register("celifern_sapling", key -> new TooltipTextBlockItem(CELIFERN_SAPLING.get(), new Item.Properties()));

    public static final DeferredBlock<Block> MANA_BERRY_BUSH = BLOCKS.register(
            "mana_berry_bush",
            () -> new ManaBerryBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)
                            .lightLevel(p_187431_ -> 5)
            )
    );

    public static final DeferredBlock<Block> MISTALIA_PETALS = BLOCKS.register(
            "mistalia_petals",
            () -> new MistaliaPetalsBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_PETALS)
            )
    );

    public static final DeferredItem<BlockItem> MISTALIA_PETALS_ITEM = ItemInit.ITEMS.register("mistalia_petals", key -> new TooltipTextBlockItem(MISTALIA_PETALS.get(), new Item.Properties()));

    public static void registerBlocks(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Blocks for" + Magitech.MOD_ID);
        BLOCKS.register(eventBus);
        BLOCK_ENITIES.register(eventBus);
    }

    public static void registerStrippableBlocks() {
        StrippableBlockRegistry.register(CELIFERN_LOG.get(), STRIPPED_CELIFERN_LOG.get());
        StrippableBlockRegistry.register(CELIFERN_WOOD.get(), STRIPPED_CELIFERN_WOOD.get());
    }
}
