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
import net.stln.magitech.magic.mana.ManaData;
import net.stln.magitech.magic.mana.ManaUtil;

import java.util.Objects;
import java.util.UUID;

public class SyncManaPayLoadHandler {

    public static void handleDataOnMainS2C(final SyncManaPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(UUID.fromString(payload.uuid()));
        ManaData.setCurrentMana(player, ManaUtil.getManaType(payload.manaType()), payload.value());
    }

    public static void handleDataOnMainC2S(final SyncManaPayload payload, final IPayloadContext context) {
        Player player = context.player();
        ManaData.setCurrentMana(player, ManaUtil.getManaType(payload.manaType()), payload.value());
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers())
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
    }
}
