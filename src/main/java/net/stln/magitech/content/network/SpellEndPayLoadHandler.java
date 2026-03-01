package net.stln.magitech.content.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.content.item.component.SpellComponent;
import net.stln.magitech.feature.magic.spell.ISpell;
import net.stln.magitech.feature.magic.spell.Spell;
import net.stln.magitech.helper.ComponentHelper;
import net.stln.magitech.helper.CuriosHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class SpellEndPayLoadHandler {

    public static void handleDataOnMainS2C(final SpellEndPayload payload, final IPayloadContext context) {
        Level level = context.player().level();
        ISpell spell = payload.spell();
        Entity entity = level.getEntity(payload.id());
        ItemStack wand = payload.wand().orElse(null);
        if (entity instanceof LivingEntity caster) {
            InteractionHand hand = caster.getMainHandItem().equals(wand) ? InteractionHand.MAIN_HAND : (caster.getOffhandItem().equals(wand) ? InteractionHand.OFF_HAND : null);
            spell.end(level, caster, wand, hand, false);
        }
    }

    public static void handleDataOnMainC2S(final SpellEndPayload payload, final IPayloadContext context) {
        Player sender = context.player();
        Level level = sender.level();
        ISpell spell = payload.spell();
        Entity entity = level.getEntity(payload.id());
        ItemStack wand = payload.wand().orElse(null);
        if (entity instanceof LivingEntity caster) {
            InteractionHand hand = caster.getMainHandItem().equals(wand) ? InteractionHand.MAIN_HAND : (caster.getOffhandItem().equals(wand) ? InteractionHand.OFF_HAND : null);
            spell.end(level, caster, wand, hand, false);
        }
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            if (sender.getUUID() != serverPlayer.getUUID()) {
                PacketDistributor.sendToPlayer(serverPlayer, payload);
            }
        }
    }
}
