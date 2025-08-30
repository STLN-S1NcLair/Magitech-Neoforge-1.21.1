package net.stln.magitech.network;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.gui.ThreadboudMenuType;
import net.stln.magitech.item.ThreadBoundItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class OpenThreadBoundPageScreenPayLoadHandler {

    public static void handleDataOnMainC2S(final OpenThreadBoundPageScreenPayload payload, final IPayloadContext context) {
        Player player = null;
        Level level = context.player().level();
        for (Player search : level.players()) {
            if (search.getUUID().toString().equals(payload.uuid())) {
                player = search;
                break;
            }
        }
        if (player == null) {
            return;
        }
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ThreadBoundItem) {
            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, player2) -> new ThreadboudMenuType(containerId, playerInventory),
                    player.getItemInHand(InteractionHand.MAIN_HAND).getDisplayName()
            ));
        } else {
            ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
            ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);
            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, player2) -> new ThreadboudMenuType(containerId, playerInventory),
                    threadbound.getDisplayName()
            ));
        }
    }
}
