package net.stln.magitech.helper;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class VectorHelper {

    // 一様分布な3次元ベクトルを取得
    public static Vec3 random(RandomSource rand) {
        double theta = Mth.randomBetween(rand, 0, Mth.TWO_PI);
        double x = Mth.randomBetween(rand, -1, 1);
        double length = Math.sqrt(1 - x * x);
        double y = Math.sin(theta) * length;
        double z = Math.cos(theta) * length;
        return new Vec3(x, y, z);
    }

    // 内部まで密な3次元ベクトル
    public static Vec3 randScaledRandom(RandomSource rand) {
        return random(rand).scale(Math.pow(rand.nextFloat(), 1.0F / 3.0F));
    }

    // 爆発用ランダムvector
    public static Vec3 blastRandom(RandomSource rand) {
        return random(rand).lerp(randScaledRandom(rand), 0.5F);
    }
}
