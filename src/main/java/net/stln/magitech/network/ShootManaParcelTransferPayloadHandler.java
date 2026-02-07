package net.stln.magitech.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.particle.particle_option.UnstableSquareParticleEffect;
import org.joml.Vector3f;

public class ShootManaParcelTransferPayloadHandler {
    // クライアント側での処理
    public static void handleDataOnMainS2C(ShootManaParcelTransferPayload packet, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        // クライアントのワールドを取得
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            spawnManaParticle(level, packet.from(), packet.direction());
        }
    }

    private static void spawnManaParticle(Level level, BlockPos fromPos, Direction direction) {
        Vec3 from = fromPos.getCenter();
        RandomSource random = level.random;
            Vector3f fromColor = new Vector3f(0.8F, 1.0F, 0.7F);
            Vector3f toColor = new Vector3f(0.0F, 1.0F, 0.9F);
            float scale = 1.0F;
            float rotSpeed = 0.0F;
            int particleAmount = 30;
            for (int i = 0; i < particleAmount; i++) {
                int twinkle = random.nextInt(2, 5);
                double mul = Mth.randomBetween(random, 0.9f, 1.1f);
                double vx = direction.getNormal().getX() + Mth.randomBetween(random, -0.5f, 0.5f) / 3;
                double vy = direction.getNormal().getY() + Mth.randomBetween(random, -0.5f, 0.5f) / 3;
                double vz = direction.getNormal().getZ() + Mth.randomBetween(random, -0.5f, 0.5f) / 3;
                Vec3 particlePos = Vec3.atLowerCornerOf(fromPos).add(Mth.randomBetween(random, 0.15F, 0.85F), Mth.randomBetween(random, 0.15F, 0.85F), Mth.randomBetween(random, 0.15F, 0.85F));
                level.addParticle(new UnstableSquareParticleEffect(fromColor, toColor, scale, twinkle, rotSpeed, random.nextInt(10, 20), Mth.randomBetween(random, 0.7f, 0.8f)), particlePos.x, particlePos.y, particlePos.z, vx, vy, vz);
            }
    }
}
