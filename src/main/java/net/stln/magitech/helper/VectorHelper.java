package net.stln.magitech.helper;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VectorHelper {

    // 一様分布なxz平面上のベクトルを取得
    public static @NotNull Vec3 randomXZ(@NotNull RandomSource rand) {
        double theta = Mth.randomBetween(rand, 0, Mth.TWO_PI);
        double x = Math.sin(theta);
        double z = Math.cos(theta);
        return new Vec3(x, 0, z);
    }

    // 一様分布な3次元ベクトルを取得
    public static @NotNull Vec3 random(@NotNull RandomSource rand) {
        double theta = Mth.randomBetween(rand, 0, Mth.TWO_PI);
        double x = Mth.randomBetween(rand, -1, 1);
        double length = Math.sqrt(1 - x * x);
        double y = Math.sin(theta) * length;
        double z = Math.cos(theta) * length;
        return new Vec3(x, y, z);
    }

    // 内部まで密な3次元ベクトル
    public static @NotNull Vec3 randScaledRandom(@NotNull RandomSource rand) {
        return random(rand).scale(Math.pow(rand.nextFloat(), 1.0F / 3.0F));
    }

    // 爆発用ランダムvector
    public static @NotNull Vec3 blastRandom(@NotNull RandomSource rand) {
        return random(rand).lerp(randScaledRandom(rand), 0.5F);
    }

    public static @NotNull Vec3 randomInCube(@NotNull RandomSource rand) {
        double x = Mth.randomBetween(rand, -0.5F, 0.5F);
        double y = Mth.randomBetween(rand, -0.5F, 0.5F);
        double z = Mth.randomBetween(rand, -0.5F, 0.5F);
        return new Vec3(x, y, z);
    }

    /**
     * 指定した軸と角度で回転を適用する
     *
     * @param vector       回転させる3Dベクトル
     * @param axis         回転軸（正規化された Vec3f）
     * @param angleDegrees 回転角度（度単位）
     * @return 回転後のベクトル
     */
    public static @NotNull Vec3 rotateVector(@NotNull Vec3 vector, @NotNull Vec3 axis, double angleDegrees) {
        // 角度をラジアンに変換
        double angleRadians = org.joml.Math.toRadians(angleDegrees);

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
