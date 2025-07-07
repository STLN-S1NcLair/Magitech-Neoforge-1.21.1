package net.stln.magitech.block;

import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
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
import net.stln.magitech.item.TooltipTextSignItem;
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
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .lightLevel(p_187431_ -> 5)
                    .sound(CRYSTAL)
                    .forceSolidOn()
                    .noOcclusion()
                    .strength(0.5F, 2.0F)
                    .requiresCorrectToolForDrops()
                    .pushReaction(PushReaction.DESTROY));

    public static final DeferredItem<BlockItem> REDSTONE_CRYSTAL_CLUSTER_ITEM = ItemInit.ITEMS.register("redstone_crystal_cluster", key -> new TooltipTextBlockItem(REDSTONE_CRYSTAL_CLUSTER.get(), new Item.Properties()));

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
    }

    public static void registerStrippableBlocks() {
        StrippableBlockRegistry.register(CELIFERN_LOG.get(), STRIPPED_CELIFERN_LOG.get());
        StrippableBlockRegistry.register(CELIFERN_WOOD.get(), STRIPPED_CELIFERN_WOOD.get());
    }
}
