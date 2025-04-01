package net.stln.magitech.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EffectUtil {

    public static void lineEffect(Level level, ParticleOptions particleOptions, Vec3 start, Vec3 end, int density, boolean alwaysVisible) {
        if (!level.isClientSide)  {
            return;
        }

        int amount = (int) (density * start.distanceTo(end));

        for (int i = 0; i < amount; i++) {
            double offset = (double) i / (amount - 1);
            Vec3 currentPos = start.add(end.subtract(start).multiply(offset, offset, offset));
            if (alwaysVisible) {
                level.addAlwaysVisibleParticle(particleOptions, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
            } else {
                level.addParticle(particleOptions, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
            }
        }
    }

    public static void entityEffect(Level level, ParticleOptions particleOptions, Entity entity, int amount) {
        for (int i = 0; i < amount; i++) {
            Vec3 randomBody = new Vec3(entity.getX() + (entity.getBbWidth() + 0.5F) * (entity.getRandom().nextFloat() - 0.5),
                    entity.getY(0.5F) + (entity.getBbHeight() + 0.5F) * (entity.getRandom().nextFloat() - 0.5),
                    entity.getZ() + (entity.getBbWidth() + 0.5F) * (entity.getRandom().nextFloat() - 0.5));

            level.addParticle(particleOptions, randomBody.x, randomBody.y, randomBody.z, 0, 0, 0);
        }
    }

    public static void sweepEffect(Player player, Level world, ParticleOptions particleEffect, Vec3 center, double startDeg, double endDeg, int density, double radius, double slopeDeg, boolean alwaysVisible) {
        if (!world.isClientSide) {
            return; // クライアント側のみ処理
        }

        Vec3 lookVec = Vec3.directionFromRotation(player.getRotationVector()); // プレイヤーの視線方向
        double yawRad = Math.toRadians(player.getRotationVector().y);

        // **視線方向に基づく「右方向ベクトル」を計算**
        Vec3 rightVec = new Vec3(Math.cos(yawRad), 0, Math.sin(yawRad)).normalize(); // 視線の右方向
        Vec3 upVec = lookVec.cross(rightVec).normalize(); // 視線に対する上方向

        for (int i = 0; i < density; i++) {
            double t = (double) i / (density - 1);
            double angleDeg = startDeg + (endDeg - startDeg) * t;

            Vec3 axisVec = rotateVector(upVec, lookVec, slopeDeg);
            Vec3 offset = rotateVector(lookVec, axisVec, angleDeg);

            double x = center.x - offset.x * radius;
            double y = center.y - offset.y * radius;
            double z = center.z - offset.z * radius;

            // **パーティクルをスポーン**
            if (alwaysVisible) {
                world.addAlwaysVisibleParticle(particleEffect, x, y, z, 0, 0, 0);
            } else {
                world.addParticle(particleEffect, x, y, z, 0, 0, 0);
            }
        }
    }

    /**
     * 指定した軸と角度で回転を適用する
     *
     * @param vector       回転させる3Dベクトル
     * @param axis         回転軸（正規化された Vec3f）
     * @param angleDegrees 回転角度（度単位）
     * @return 回転後のベクトル
     */
    public static Vec3 rotateVector(Vec3 vector, Vec3 axis, double angleDegrees) {
        // 角度をラジアンに変換
        double angleRadians = Math.toRadians(angleDegrees);

        // クォータニオンを作成（回転軸 + 角度）
        Quaternionf rotation = new Quaternionf(axis.x, axis.y, axis.z, angleRadians);

        // Vec3 → Vec3f（Minecraftのクォータニオン適用用）
        Vector3f vec = new Vector3f((float) vector.x, (float) vector.y, (float) vector.z);

        // クォータニオンを適用（回転）
        vec.rotate(rotation);

        // 回転後の Vec3f → Vec3 に戻す
        return new Vec3(vec.x, vec.y, vec.z);
    }
}
