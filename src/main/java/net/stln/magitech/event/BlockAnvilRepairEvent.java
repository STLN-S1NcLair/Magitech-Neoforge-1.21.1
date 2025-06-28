package net.stln.magitech.event;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class BlockAnvilRepairEvent {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        // 左または右に特定のアイテムがある場合、処理を無効化
        if (left.getItem() instanceof PartToolItem || right.getItem() instanceof PartToolItem) {
            event.setCanceled(true); // 金床の出力をブロック
        }
    }
}
