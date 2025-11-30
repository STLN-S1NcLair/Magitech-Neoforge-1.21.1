package net.stln.magitech.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.stln.magitech.item.tool.trait.SparkTrait;
import net.stln.magitech.util.CuriosHelper;
import net.stln.magitech.util.ToolBeltHelper;

import java.util.Objects;

public class SparkTraitBeamPayloadHandler {

    public static void handleDataOnMainS2C(final SparkTraitBeamPayload payload, final IPayloadContext context) {
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
        SparkTrait.playAnim(player, level);
        SparkTrait.addVisualEffect(level, player, payload.pos(), payload.hitPos());
    }
}
