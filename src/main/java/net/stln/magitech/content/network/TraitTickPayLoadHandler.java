package net.stln.magitech.content.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.feature.tool.trait.TraitHelper;

import java.util.Objects;

public class TraitTickPayLoadHandler {

    public static void handleDataOnMainS2C(final TraitTickPayload payload, final IPayloadContext context) {
        Player player = null;
        Level level = context.player().level();
        for (Player search : level.players()) {
            if (Objects.equals(search.getUUID(), payload.uuid())) {
                player = search;
                break;
            }
        }
        Player player1 = player;
        if (player == null) {
            return;
        }
        ItemStack stack = player.getInventory().getItem(payload.slot());
        if (stack.getItem() instanceof SynthesisedToolItem) {
            if (payload.isInventory()) {
                TraitHelper.getTrait(stack).forEach((instance) -> instance.trait().inventoryTick(player1, player1.level(), stack, instance.level(), ((SynthesisedToolItem) stack.getItem()).getAppliedProperties(player1, player1.level(), stack), false));
            } else {
                TraitHelper.getTrait(stack).forEach((instance) -> instance.trait().handTick(player1, player1.level(), stack, instance.level(), ((SynthesisedToolItem) stack.getItem()).getAppliedProperties(player1, player1.level(), stack), false));
            }
        }
    }

    public static void handleDataOnMainC2S(final TraitTickPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(payload.uuid());
        if (player == null) {
            return;
        }
        ItemStack stack = player.getInventory().getItem(payload.slot());
        if (stack.getItem() instanceof SynthesisedToolItem) {
            if (payload.isInventory()) {
                TraitHelper.getTrait(stack).forEach((instance) -> instance.trait().inventoryTick(player, player.level(), stack, instance.level(), ((SynthesisedToolItem) stack.getItem()).getAppliedProperties(player, player.level(), stack), false));
            } else {
                TraitHelper.getTrait(stack).forEach((instance) -> instance.trait().handTick(player, player.level(), stack, instance.level(), ((SynthesisedToolItem) stack.getItem()).getAppliedProperties(player, player.level(), stack), false));
            }
        }
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers())
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
    }
}
