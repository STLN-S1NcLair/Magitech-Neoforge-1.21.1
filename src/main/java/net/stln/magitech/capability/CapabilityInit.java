package net.stln.magitech.capability;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.api.Capabilities;
import net.stln.magitech.api.mana.ItemManaHandler;
import net.stln.magitech.block.BlockInit;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.energy.ManaContainerItem;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class CapabilityInit {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // ブロックエンティティへの登録
        event.registerBlockEntity(
                Capabilities.MANA_CONTAINER,
                BlockInit.MANA_VESSEL_ENTITY.get(),
                (be, context) -> be
        );

        // アイテムへの登録
        event.registerItem(
                Capabilities.MANA_CONTAINER_ITEM, // ItemCapabilityの方を使う
                // コンストラクタで maxFlow を指定 (例: 容量10000, 流量100)
                (stack, context) -> {
                    if (stack.getItem() instanceof ManaContainerItem manaContainerItem) {
                        // Itemクラスに設定された数値を使ってHandlerを作る
                        return new ItemManaHandler(stack, manaContainerItem.getMaxMana(), manaContainerItem.getMaxFlow());
                    }
                    // 万が一違うアイテムならデフォルト値 (通常ここは通らない)
                    return new ItemManaHandler(stack, 1000, 10);
                },
                ItemInit.MANA_CELL.get()
        );
    }
}
