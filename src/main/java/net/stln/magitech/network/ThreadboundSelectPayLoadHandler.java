package net.stln.magitech.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.CuriosHelper;

import java.util.Objects;

public class ThreadboundSelectPayLoadHandler {

    public static void handleDataOnMainS2C(final ThreadboundSelectPayload payload, final IPayloadContext context) {
        Player player = null;
        Level level = context.player().level();
        for (Player search : level.players()) {
            if (Objects.equals(search.getUUID(), payload.uuid())) {
                player = search;
                break;
            }
        }
        if (player == null) {
            return;
        }
        CuriosHelper.getThreadBoundStack(player).ifPresent(stack -> ComponentHelper.updateSpells(stack, spellComponent -> spellComponent.setSelected(payload.select())));
    }

    public static void handleDataOnMainC2S(final ThreadboundSelectPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(payload.uuid());
        if (player == null) return;

        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
        }
        CuriosHelper.getThreadBoundStack(player).ifPresent(stack -> ComponentHelper.updateSpells(stack, spellComponent -> spellComponent.setSelected(payload.select())));
    }
}
