package net.stln.magitech.capability;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.ManaCapabilities;
import net.stln.magitech.api.mana.handler.ManaContainerItemManaHandler;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.block.block_entity.ManaContainerBlockEntity;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.energy.ManaContainerItem;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class CapabilityInit {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.TOOL_HANGER_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.ALCHEMETRIC_PYLON_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.ATHANOR_PILLAR_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.ZARDIUS_CRUCIBLE_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, BlockInit.ZARDIUS_CRUCIBLE_ENTITY.get(), (blockEntity, direction) -> blockEntity.fluidTank);

        // ブロックエンティティへの登録
        registerManaContainerBlockEntity(event, BlockInit.MANA_VESSEL_ENTITY.get());

        // アイテムへの登録
        registerManaContainerItem(event, (ManaContainerItem) ItemInit.MANA_CELL.get());
    }

    private static <T extends ManaContainerBlockEntity> void registerManaContainerBlockEntity(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
        event.registerBlockEntity(
                ManaCapabilities.MANA_CONTAINER,
                type,
                ManaContainerBlockEntity::getManaHandler
        );
    }

    private static  <T extends ManaContainerItem> void registerManaContainerItem(RegisterCapabilitiesEvent event, T item) {
        event.registerItem(
                ManaCapabilities.MANA_CONTAINER_ITEM,
                (stack, context) -> new ManaContainerItemManaHandler(stack),
                item
        );
    }
}
