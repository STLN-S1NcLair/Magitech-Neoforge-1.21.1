package net.stln.magitech.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.item.ItemInit;
import net.stln.magitech.item.armor.AetherLifterItem;

import java.util.Objects;
import java.util.UUID;

public class DoubleJumpPayLoadHandler {

    public static void handleDataOnMainS2C(final DoubleJumpPayload payload, final IPayloadContext context) {
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
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (boots.getItem() == ItemInit.AETHER_LIFTER.get()) {
            AetherLifterItem.doubleJump(player, payload.jumpCount(), boots);
        }
    }

    public static void handleDataOnMainC2S(final DoubleJumpPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(UUID.fromString(payload.uuid()));
        if (player == null) {
            return;
        }
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (boots.getItem() == ItemInit.AETHER_LIFTER.get()) {
            AetherLifterItem.doubleJump(player, payload.jumpCount(), boots);
        }
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers())
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
    }
}
