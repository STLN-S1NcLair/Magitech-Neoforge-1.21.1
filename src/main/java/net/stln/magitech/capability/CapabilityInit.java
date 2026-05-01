package net.stln.magitech.capability;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.BlockInit;
import net.stln.magitech.content.block.block_entity.ManaContainerBlockEntity;
import net.stln.magitech.content.item.ItemInit;
import net.stln.magitech.content.item.energy.ManaContainerItem;
import net.stln.magitech.core.api.mana.ManaCapabilities;
import net.stln.magitech.core.api.mana.handler.EntityManaHandler;
import net.stln.magitech.core.api.mana.handler.ManaContainerItemManaHandler;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class CapabilityInit {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.TOOL_HANGER_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.ALCHEMETRIC_PYLON_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.INFUSER_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.PEDESTAL_PYLON_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.INFUSION_ALTAR_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.ZARDIUS_CRUCIBLE_ENTITY.get(), (blockEntity, direction) -> blockEntity.inventory);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, BlockInit.ZARDIUS_CRUCIBLE_ENTITY.get(), (blockEntity, direction) -> blockEntity.tank);

        // ブロックエンティティへの登録
        registerManaContainerBlockEntity(event, BlockInit.MANA_VESSEL_ENTITY.get());
        registerManaContainerBlockEntity(event, BlockInit.MANA_STRANDER_ENTITY.get());
        registerManaContainerBlockEntity(event, BlockInit.MANA_RECEIVER_ENTITY.get());
        registerManaContainerBlockEntity(event, BlockInit.MANA_COLLECTOR_ENTITY.get());
        registerManaContainerBlockEntity(event, BlockInit.INFUSION_ALTAR_ENTITY.get());
        registerManaContainerBlockEntity(event, BlockInit.ENHANCED_MANA_VESSEL_ENTITY.get());
        registerManaContainerBlockEntity(event, BlockInit.INFUSER_ENTITY.get());
        registerManaContainerBlockEntity(event, BlockInit.ZARDIUS_CRUCIBLE_ENTITY.get());

        // アイテムへの登録
        registerManaContainerItem(event, (ManaContainerItem) ItemInit.MANA_CELL.get());

        // エンティティへの登録
        registerManaCapableEntity(event, EntityType.PLAYER);
    }

    private static <T extends ManaContainerBlockEntity> void registerManaContainerBlockEntity(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
        event.registerBlockEntity(
                ManaCapabilities.MANA_CONTAINER,
                type,
                ManaContainerBlockEntity::getManaHandler
        );
    }

    private static <T extends ManaContainerItem> void registerManaContainerItem(RegisterCapabilitiesEvent event, T item) {
        event.registerItem(
                ManaCapabilities.MANA_CONTAINER_ITEM,
                (stack, context) -> new ManaContainerItemManaHandler(stack),
                item
        );
    }

    private static void registerManaCapableEntity(RegisterCapabilitiesEvent event, EntityType<?> entityType) {
        event.registerEntity(
                ManaCapabilities.MANA_CAPABLE_ENTITY,
                entityType,
                (entity, context) -> {
                    if (entity instanceof LivingEntity livingEntity) {
                        return new EntityManaHandler(livingEntity);
                    }
                    return null;
                }
        );
    }
}
