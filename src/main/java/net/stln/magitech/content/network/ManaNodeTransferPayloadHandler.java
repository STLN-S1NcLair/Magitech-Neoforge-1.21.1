package net.stln.magitech.content.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.preset.TrailVFX;
import net.stln.magitech.feature.element.Element;

public class ManaNodeTransferPayloadHandler {

    // クライアント側での処理
    public static void handleDataOnMainS2C(ManaNodeTransferPayload packet, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        // クライアントのワールドを取得
        Level level = context.player().level();
        if (level != null) {
            spawnManaParticle(level, packet.from(), packet.to());
        }
    }

    private static void spawnManaParticle(Level level, BlockPos fromPos, BlockPos toPos) {
        if (level.getRandom().nextFloat() < 0.5F || level.getGameTime() % 5 == 0) {
            Vec3 from = fromPos.getCenter();
            Vec3 to = toPos.getCenter();
            TrailVFX.zapTrail(level, from, to, 0.25F, 1.5F, 0.25F, 10, Element.MANA);
        }
    }
}
