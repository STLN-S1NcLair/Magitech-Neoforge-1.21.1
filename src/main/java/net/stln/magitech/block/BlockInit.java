package net.stln.magitech.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.sound.SoundInit;

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

    public static final DeferredItem<BlockItem> ENGINEERING_WORKBENCH_ITEM = ItemInit.ITEMS.registerSimpleBlockItem("engineering_workbench", ENGINEERING_WORKBENCH);

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

    public static final DeferredItem<BlockItem> REDSTONE_CRYSTAL_CLUSTER_ITEM = ItemInit.ITEMS.registerSimpleBlockItem("redstone_crystal_cluster", REDSTONE_CRYSTAL_CLUSTER);

    public static void registerBlocks(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Blocks for" + Magitech.MOD_ID);
        BLOCKS.register(eventBus);
    }
}
