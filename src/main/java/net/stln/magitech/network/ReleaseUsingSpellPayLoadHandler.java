package net.stln.magitech.network;

import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.item.component.SpellComponent;
import net.stln.magitech.magic.spell.Spell;
import net.stln.magitech.util.ComponentHelper;
import net.stln.magitech.util.CuriosHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class ReleaseUsingSpellPayLoadHandler {

    public static void handleDataOnMainS2C(final ReleaseUsingSpellPayload payload, final IPayloadContext context) {
        Level level = context.player().level();
        Player player = level.players().stream().filter(search -> Objects.equals(search.getUUID(), payload.uuid())).findFirst().orElse(null);
        if (player == null) return;
        getSpell(player).ifPresent(spell -> spell.value().finishUsing(payload.stack(), player.level(), player, payload.chargeTime(), false));
    }

    public static void handleDataOnMainC2S(final ReleaseUsingSpellPayload payload, final IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(payload.uuid());
        if (player == null) return;
        getSpell(player).ifPresent(spell -> spell.value().finishUsing(payload.stack(), player.level(), player, payload.chargeTime(), false));
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers())
            if (player.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
    }

    private static Optional<Holder<Spell>> getSpell(@NotNull Player player) {
        return CuriosHelper.getThreadBoundStack(player).map(stack -> {
            SpellComponent spellComponent = ComponentHelper.getSpells(stack);
            return spellComponent.spells().get(spellComponent.selected());
        });
    }
}
