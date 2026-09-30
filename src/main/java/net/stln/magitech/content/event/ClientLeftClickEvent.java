package net.stln.magitech.content.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.item.LeftClickOverrideItem;
import net.stln.magitech.content.network.LeftClickPayload;

@EventBusSubscriber(modid = Magitech.MOD_ID, value = Dist.CLIENT)
public class ClientLeftClickEvent {

    @SubscribeEvent
    public static void onLeftClick(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isAttack()) {
            Player player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }
            LeftClickPayload payload = new LeftClickPayload(player.getUUID());
            PacketDistributor.sendToServer(payload);
            if (callOnLeftClick(player) == InteractionResult.SUCCESS) {
                event.setCanceled(true);
            }
        }
    }


    public static InteractionResult callOnLeftClick(Player player) {
        if (!player.isUsingItem() && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LeftClickOverrideItem leftClickOverrideItem) {
            return leftClickOverrideItem.onLeftClick(player, InteractionHand.MAIN_HAND, player.level());
        }
        return InteractionResult.PASS;
    }
}
