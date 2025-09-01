package net.stln.magitech.event;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class AddBlocksToBlockEntityEvent {

    @SubscribeEvent
    public static void addBlocksToBlockEntity(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityType.SIGN, BlockInit.CELIFERN_SIGN.get(), BlockInit.CELIFERN_WALL_SIGN.get());
        event.modify(BlockEntityType.HANGING_SIGN, BlockInit.CELIFERN_HANGING_SIGN.get(), BlockInit.CELIFERN_WALL_HANGING_SIGN.get());
    }

}
