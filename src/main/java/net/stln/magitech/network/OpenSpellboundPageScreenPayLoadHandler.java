package net.stln.magitech.network;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.gui.ThreadboudMenuType;
import net.stln.magitech.item.ThreadboundItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class OpenSpellboundPageScreenPayLoadHandler {

    public static void handleDataOnMainC2S(final OpenSpellboundPageScreenPayload payload, final IPayloadContext context) {
        Player player = context.player();
        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ThreadboundItem) {
            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, player2) -> new ThreadboudMenuType(containerId, playerInventory),
                    Component.literal(player.getItemInHand(InteractionHand.MAIN_HAND).getHoverName().getString())
            ));
        } else {
            ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
            ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);
            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, player2) -> new ThreadboudMenuType(containerId, playerInventory),
                    Component.literal(threadbound.getHoverName().getString())
            ));
        }
    }
}
