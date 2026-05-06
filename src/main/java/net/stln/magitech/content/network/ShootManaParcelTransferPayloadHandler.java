package net.stln.magitech.content.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.preset.PointVFX;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;

public class ShootManaParcelTransferPayloadHandler {
    // クライアント側での処理
    public static void handleDataOnMainS2C(ShootManaParcelTransferPayload packet, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        // クライアントのワールドを取得
        Level level = context.player().level();
        if (level != null) {
            spawnManaParticle(level, packet.from(), packet.direction());
        }
    }

    private static void spawnManaParticle(Level level, BlockPos fromPos, Direction direction) {
        Vec3 from = fromPos.getCenter();
        PointVFX.spray(level, from.add(Vec3.atLowerCornerOf(direction.getNormal()).scale(0.1F)), Element.MANA, SquareParticles::squareParticle, Vec3.atLowerCornerOf(direction.getNormal()), 20, 0.2F, 0.1F);
    }
}
