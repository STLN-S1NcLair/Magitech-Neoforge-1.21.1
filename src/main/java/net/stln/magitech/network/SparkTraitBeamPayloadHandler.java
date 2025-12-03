package net.stln.magitech.network;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.stln.magitech.item.tool.trait.SparkTrait;

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
