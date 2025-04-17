package net.stln.magitech.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.item.component.ComponentInit;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.magic.spell.Spell;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Objects;
import java.util.UUID;

public class UseSpellPayLoadHandler {

    public static void handleDataOnMainS2C(final UseSpellPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(UUID.fromString(payload.uuid()));
        InteractionHand hand = payload.isMainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
        ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);

        SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);

        Spell spell = spellComponent.spells().get(spellComponent.selected());

        spell.use(player.level(), player, hand, false);
    }

    public static void handleDataOnMainC2S(final UseSpellPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(UUID.fromString(payload.uuid()));
        InteractionHand hand = payload.isMainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).get();
        ItemStack threadbound = curiosInventory.getCurios().get("threadbound").getStacks().getStackInSlot(0);

        SpellComponent spellComponent = threadbound.get(ComponentInit.SPELL_COMPONENT);

        Spell spell = spellComponent.spells().get(spellComponent.selected());

        spell.use(player.level(), player, hand, false);
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers())
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
    }
}
