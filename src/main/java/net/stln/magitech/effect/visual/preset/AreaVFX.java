package net.stln.magitech.effect.visual.preset;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.spawner.RayParticles;
import net.stln.magitech.feature.element.Element;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.world.behaviors.DirectionalParticleBehavior;

public class AreaVFX {

    public static void areaLight(Level level, Element element, Vec3 pos, float radius, float height, int lifetime) {
        int sides = getSidesFromInRadius(radius);
        for (int i = 0; i < sides; i++) {
            float theta = Mth.TWO_PI * i / sides;
            Vec3 normal = new Vec3(Mth.sin(theta), 0, Mth.cos(theta));
            ParticleEffectSpawner spawner = RayParticles.rayParticle(level, pos.add(normal.scale(radius)), element.getPrimary(), element.getSecondary(), getSideLength(radius, sides), height, lifetime);
            PresetHelper.modify(spawner, builder -> builder.setBehavior(DirectionalParticleBehavior.directional(normal)));
            spawner.spawnParticles();
        }
    }
    public static void areaLight(Level level, Element element, Vec3 pos, float radius, int lifetime) {
        areaLight(level, element, pos, radius, 1.0F, lifetime);
    }

    public static int getSidesFromInRadius(float radius) {
        return Math.max(36, (int) Math.ceil(8 * Math.PI * radius));
    }

    public static float getSideLength(float inRadius, int sides) {
        if (sides < 3) {
            throw new IllegalArgumentException("sides must be >= 3");
        }
        return (float) (getCorrectedRadius(inRadius, sides) * Math.tan(Math.PI / sides));
    }

    public static double getCorrectedRadius(double inradius, int sides) {
        if (sides < 3) {
            throw new IllegalArgumentException("sides must be >= 3");
        }
        double theta = Math.PI / sides;
        return inradius * (theta / Math.sin(theta));
    }
}
