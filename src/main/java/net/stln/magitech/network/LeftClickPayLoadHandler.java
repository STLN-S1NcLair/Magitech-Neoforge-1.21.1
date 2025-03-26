package net.stln.magitech.network;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.item.LeftClickOverrideItem;

public class LeftClickPayLoadHandler {

    public static void handleDataOnMain(final LeftClickC2SPayload payload, final IPayloadContext context) {
        Player player = context.player();
        Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        if (item instanceof LeftClickOverrideItem clickOverrideItem) {
            clickOverrideItem.onLeftClick(player, InteractionHand.MAIN_HAND, player.level());
        }
    }
}
