package net.stln.magitech.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.ItemInit;

public class BlockInit {


    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Magitech.MOD_ID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerBlock("example_block",
            Block::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE));

    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ItemInit.ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    public static void registerBlocks(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Blocks for" + Magitech.MOD_ID);
        BLOCKS.register(eventBus);
    }
}
