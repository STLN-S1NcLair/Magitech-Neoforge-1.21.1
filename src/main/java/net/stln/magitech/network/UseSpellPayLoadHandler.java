package net.stln.magitech.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.CuriosHelper;

import java.util.Objects;
import java.util.UUID;

public class UseSpellPayLoadHandler {

    public static void handleDataOnMainS2C(final UseSpellPayload payload, final IPayloadContext context) {
        Level level = context.player().level();
        Player player = level.getPlayerByUUID(UUID.fromString(payload.uuid()));
        InteractionHand hand = payload.isMainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        CuriosHelper.getThreadBoundStack(player).ifPresent(stack -> {
            SpellComponent spellComponent = ComponentHelper.getSpells(stack);
            Spell spell = spellComponent.spells().get(spellComponent.selected());
            spell.use(level, player, hand, false);
        });
    }

    public static void handleDataOnMainC2S(final UseSpellPayload payload, final IPayloadContext context) {
        Level level = context.player().level();
        Player player = level.getPlayerByUUID(UUID.fromString(payload.uuid()));
        if (player == null) return;
        InteractionHand hand = payload.isMainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        CuriosHelper.getThreadBoundStack(player).ifPresent(stack -> {
            SpellComponent spellComponent = ComponentHelper.getSpells(stack);
            Spell spell = spellComponent.spells().get(spellComponent.selected());
            spell.use(level, player, hand, false);
            MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
            for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
                if (player.getUUID() != serverPlayer.getUUID()) {
                    PacketDistributor.sendToPlayer(serverPlayer, payload);
                }
            }
        });
    }
}
