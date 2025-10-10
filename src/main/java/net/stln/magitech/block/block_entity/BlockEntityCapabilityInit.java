package net.stln.magitech.block.block_entity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.BlockInit;


@EventBusSubscriber(modid = Magitech.MOD_ID)
public class BlockEntityCapabilityInit {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.ALCHEMETRIC_PYLON_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.ATHANOR_PILLAR_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.ZARDIUS_CRUCIBLE_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, BlockInit.ZARDIUS_CRUCIBLE_ENTITY.get(), (blockEntity, direction) -> blockEntity.fluidTank);
    }
}
