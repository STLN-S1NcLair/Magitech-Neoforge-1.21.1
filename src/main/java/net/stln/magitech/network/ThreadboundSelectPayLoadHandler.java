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
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Objects;
import java.util.UUID;

public class ThreadboundSelectPayLoadHandler {

    public static void handleDataOnMainS2C(final ThreadBoundSelectPayload payload, final IPayloadContext context) {
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
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
        ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);

        if (threadbound.has(ComponentInit.SPELL_COMPONENT)) {
            SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);
            threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spellComponent.spells(), payload.select()));
        }
    }

    public static void handleDataOnMainC2S(final ThreadBoundSelectPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(UUID.fromString(payload.uuid()));
        if (player == null) {
            return;
        }
        Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();

        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
        }
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
        ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);

        if (threadbound.has(ComponentInit.SPELL_COMPONENT)) {
            SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);
            threadbound.set(ComponentInit.SPELL_COMPONENT, new SpellComponent(spellComponent.spells(), payload.select()));
        }
    }
}
