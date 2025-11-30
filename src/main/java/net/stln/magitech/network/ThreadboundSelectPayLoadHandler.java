package net.stln.magitech.network;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.Magitech;
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
        player.releaseUsingItem();
        if (level.isClientSide) {
            var playerAnimationData = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(Magitech.id("animation"));
            if (playerAnimationData != null && playerAnimationData.getAnimation() instanceof KeyframeAnimationPlayer keyframeAnimationPlayer) {

                keyframeAnimationPlayer.stop();
            }
        }
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
        player.releaseUsingItem();
    }
}
