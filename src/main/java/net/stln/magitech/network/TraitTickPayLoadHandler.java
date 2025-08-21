package net.stln.magitech.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.Magitech;
import net.stln.magitech.item.tool.toolitem.PartToolItem;

import java.util.Objects;
import java.util.UUID;

public class TraitTickPayLoadHandler {

    public static void handleDataOnMainS2C(final TraitTickPayload payload, final IPayloadContext context) {
        Player player = null;
        Level level = context.player().level();
        for (Player search : level.players()) {
            if (search.getUUID().toString().equals(payload.uuid())) {
                player = search;
                break;
            }
        }
        Player player1 = player;
        if (player == null) {
            return;
        }
        ItemStack stack = player.getInventory().getItem(payload.slot());
        if (payload.isInventory() && stack.getItem() instanceof PartToolItem partToolItem) {
            PartToolItem.getTraitLevel(PartToolItem.getTraits(stack)).forEach((trait, integer) -> {
                trait.inventoryTick(player1, player1.level(), stack, integer, ((PartToolItem) stack.getItem()).getSumStats(player1, player1.level(), stack), false);
            });
        } else if (stack.getItem() instanceof PartToolItem partToolItem) {
                PartToolItem.getTraitLevel(PartToolItem.getTraits(stack)).forEach((trait, integer) -> {
                    trait.tick(player1, player1.level(), stack, integer, ((PartToolItem) stack.getItem()).getSumStats(player1, player1.level(), stack), false);
                });
            }
        }

    public static void handleDataOnMainC2S(final TraitTickPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(UUID.fromString(payload.uuid()));
        if (player == null) {
            return;
        }
        ItemStack stack = player.getInventory().getItem(payload.slot());
        if (payload.isInventory() && stack.getItem() instanceof PartToolItem partToolItem) {
            PartToolItem.getTraitLevel(PartToolItem.getTraits(stack)).forEach((trait, integer) -> {
                trait.inventoryTick(player, player.level(), stack, integer, ((PartToolItem) stack.getItem()).getSumStats(player, player.level(), stack), false);
            });
        } else if (stack.getItem() instanceof PartToolItem partToolItem) {
            PartToolItem.getTraitLevel(PartToolItem.getTraits(stack)).forEach((trait, integer) -> {
                trait.tick(player, player.level(), stack, integer, ((PartToolItem) stack.getItem()).getSumStats(player, player.level(), stack), false);
            });
        }
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers())
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
    }
}
