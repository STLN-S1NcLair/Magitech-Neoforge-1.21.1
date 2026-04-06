package net.stln.magitech.helper;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.Section;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class EffectHelper {

    public static void lineEffect(Level level, ParticleOptions particleOptions, Vec3 start, Vec3 end, int density, boolean alwaysVisible) {
        if (!level.isClientSide) {
            return;
        }

        int amount = (int) (density * start.distanceTo(end));

        for (int i = 0; i < amount; i++) {
            double offset = (double) i / (amount - 1);
            Vec3 currentPos = start.add(end.subtract(start).multiply(offset, offset, offset));
            displayParticle(level, particleOptions, alwaysVisible, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
        }
    }

    public static void entityEffect(Level level, ParticleOptions particleOptions, Entity entity, int amount) {
        entityEffect(level, () -> particleOptions, () -> new Vec3(0, 0, 0), entity, amount);
    }

    public static void entityEffect(Level level, Supplier<ParticleOptions> particleOptions, Supplier<Vec3> vector, Entity entity, int amount) {
        for (int i = 0; i < amount; i++) {
            Vec3 vec3 = vector.get();
            Vec3 randomBody = getRandomBody(entity);

            level.addParticle(particleOptions.get(), randomBody.x, randomBody.y, randomBody.z, vec3.x, vec3.y, vec3.z);
        }
    }

    public static @NotNull Vec3 getRandomBody(Entity entity) {
        return getRandomBody(entity, Section.cover());
    }

    public static @NotNull Vec3 getRandomBody(Entity entity, Section section) {
        return new Vec3(entity.getX() + (entity.getBbWidth() + 0.5F) * (entity.getRandom().nextFloat() - 0.5),
                entity.getY(0.5F) + (entity.getBbHeight() + 0.5F) * (Mth.randomBetween(entity.getRandom(), section.start(), section.end()) - 0.5F),
                entity.getZ() + (entity.getBbWidth() + 0.5F) * (entity.getRandom().nextFloat() - 0.5));
    }

    private static void displayParticle(Level world, ParticleOptions particleEffect, boolean alwaysVisible, double x, double y, double z, double xOffset, double yOffset, double zOffset) {
        // **パーティクルをスポーン**
        if (world != null) {
            if (alwaysVisible) {
                world.addAlwaysVisibleParticle(particleEffect, x, y, z, xOffset, yOffset, zOffset);
            } else {
                world.addParticle(particleEffect, x, y, z, xOffset, yOffset, zOffset);
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
