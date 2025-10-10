package net.stln.magitech.event;

import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.stln.magitech.item.LeftClickOverrideItem;
import net.stln.magitech.network.LeftClickPayload;

public class ClientLeftClickEvent {

    public static void register() {
        ClientPreAttackCallback.EVENT.register(((client, player, clickCount) -> {
            LeftClickPayload payload = new LeftClickPayload(clickCount, player.getUUID());
            PacketDistributor.sendToServer(payload);
            return callOnLeftClick(player, clickCount) != InteractionResult.PASS;
        }));
    }


    public static InteractionResult callOnLeftClick(Player player, int clickCount) {
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof LeftClickOverrideItem leftClickOverrideItem) {
            if (clickCount != 0) {
                return leftClickOverrideItem.onLeftClick(player, InteractionHand.MAIN_HAND, player.level());
            }
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }
}
