package net.stln.magitech.helper;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class VectorHelper {

    // 一様分布な3次元ベクトルを取得
    public static Vec3 random() {
        Random rand = new Random();
        double theta = rand.nextDouble(Mth.TWO_PI);
        double x = rand.nextDouble(-1, 1);
        double length = Math.sqrt(1 - x * x);
        double y = Math.sin(theta) * length;
        double z = Math.cos(theta) * length;
        return new Vec3(x, y, z);
    }
}
