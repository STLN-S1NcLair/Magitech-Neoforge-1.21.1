package net.stln.magitech.content.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.content.item.tool.toolitem.SynthesisedToolItem;
import net.stln.magitech.feature.tool.trait.TraitHelper;

import java.util.Objects;

public class TraitActionPayLoadHandler {

    public static void handleDataOnMainS2C(final TraitActionPayload payload, final IPayloadContext context) {
        Player player = null;
        Level level = context.player().level();
        for (Player search : level.players()) {
            if (search.getUUID().equals(payload.uuid())) {
                player = search;
                break;
            }
        }
        Player player1 = player;
        if (player == null) {
            return;
        }
        Entity entity = player.level().getEntity(payload.targetId());
        InteractionHand hand = payload.isMainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        Item item = player.getItemInHand(hand).getItem();
        if (item instanceof SynthesisedToolItem partToolItem) {
            ItemStack stack = player.getItemInHand(hand);
            TraitHelper.getTrait(stack).forEach((instance) -> {
                Vec3 lookingPos = payload.targetPos();
                if (lookingPos.x == Double.MAX_VALUE && lookingPos.y == Double.MAX_VALUE && lookingPos.z == Double.MAX_VALUE) {
                    lookingPos = null;
                }
                instance.trait().traitAction(player1, player1.level(), entity, lookingPos, stack, instance.level(), ((SynthesisedToolItem) stack.getItem()).getAppliedProperties(player1, player1.level(), stack), hand, false);
            });
        }
    }

    public static void handleDataOnMainC2S(final TraitActionPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(payload.uuid());
        if (player == null) {
            return;
        }
        Entity entity = player.level().getEntity(payload.targetId());
        InteractionHand hand = payload.isMainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        Item item = player.getItemInHand(hand).getItem();
        if (item instanceof SynthesisedToolItem partToolItem) {
            ItemStack stack = player.getItemInHand(hand);

            TraitHelper.getTrait(stack).forEach((instance) -> {
                Vec3 lookingPos = payload.targetPos();
                if (lookingPos.x == Double.MAX_VALUE && lookingPos.y == Double.MAX_VALUE && lookingPos.z == Double.MAX_VALUE) {
                    lookingPos = null;
                }
                instance.trait().traitAction(player, player.level(), entity, lookingPos, stack, instance.level(), ((SynthesisedToolItem) stack.getItem()).getAppliedProperties(player, player.level(), stack), hand, false);
            });
        }
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers())
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
    }
}
