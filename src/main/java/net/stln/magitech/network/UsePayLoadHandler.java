package net.stln.magitech.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.Objects;
import java.util.UUID;

public class UsePayLoadHandler {

    public static void handleDataOnMainS2C(final UsePayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(UUID.fromString(payload.uuid()));
        InteractionHand hand = payload.isMainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        Item item = player.getItemInHand(hand).getItem();
        if (item instanceof PartToolItem partToolItem) {
            ItemStack stack = player.getItemInHand(hand);
            PartToolItem.getTraitLevel(PartToolItem.getTraits(stack)).forEach((trait, integer) -> {
                trait.use(player, player.level(), stack, integer, PartToolItem.getSumStats(player, player.level(), stack), hand);
            });
        }
    }

    public static void handleDataOnMainC2S(final UsePayload payload, final IPayloadContext context) {
        Player player = context.player();
        InteractionHand hand = payload.isMainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        Item item = player.getItemInHand(hand).getItem();
        if (item instanceof PartToolItem partToolItem) {
            ItemStack stack = player.getItemInHand(hand);

            PartToolItem.getTraitLevel(PartToolItem.getTraits(stack)).forEach((trait, integer) -> {
                trait.use(player, player.level(), stack, integer, PartToolItem.getSumStats(player, player.level(), stack), hand);
            });
        }
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers())
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
    }
}
