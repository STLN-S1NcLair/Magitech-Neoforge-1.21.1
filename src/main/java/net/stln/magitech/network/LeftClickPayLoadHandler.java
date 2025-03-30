package net.stln.magitech.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.item.LeftClickOverrideItem;

import java.util.Objects;
import java.util.UUID;

public class LeftClickPayLoadHandler {

    public static void handleDataOnMainS2C(final LeftClickC2SPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(UUID.fromString(payload.uuid()));
        Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        if (item instanceof LeftClickOverrideItem clickOverrideItem && payload.clickCount() != 0) {
            clickOverrideItem.onLeftClick(player, InteractionHand.MAIN_HAND, player.level());
        }
    }

    public static void handleDataOnMainC2S(final LeftClickC2SPayload payload, final IPayloadContext context) {
        Player player = context.player();
        Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        if (item instanceof LeftClickOverrideItem clickOverrideItem && payload.clickCount() != 0) {
            clickOverrideItem.onLeftClick(player, InteractionHand.MAIN_HAND, player.level());
        }
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers())
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
    }
}
