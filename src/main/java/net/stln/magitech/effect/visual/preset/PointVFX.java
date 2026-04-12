package net.stln.magitech.effect.visual.preset;

import com.mojang.datafixers.util.Function3;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.VectorHelper;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.util.function.Consumer;

public class PointVFX {
    public static void vortex(Level level, Vec3 pos, Element element, Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier, int amount, float acceleration, float randomness) {
        for (int i = 0; i < amount; i++) {
            Vec3 motion = VectorHelper.random(level.random).scale(randomness);
            Consumer<LodestoneWorldParticle> behavior = BehaviorPreset.toDestination(pos, acceleration);
            ParticleEffectSpawner spawner = supplier.apply(level, pos, element);
            PresetHelper.modify(spawner, builder -> builder.setMotion(motion).addTickActor(behavior));
            spawner.spawnParticles();
        }
    }

    public static void vortexSquare(Level level, Vec3 pos, Element element, int amount, float acceleration, float randomness) {
        vortex(level, pos, element, SquareParticles::squareParticle, amount, acceleration, randomness);
    }

    public static void burst(Level level, Vec3 pos, Element element, Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier, int amount, float randomness) {
        for (int i = 0; i < amount; i++) {
            Vec3 motion = VectorHelper.blastRandom(level.random).scale(randomness);
            ParticleEffectSpawner spawner = supplier.apply(level, pos, element);
            PresetHelper.modify(spawner, builder -> builder.setMotion(motion));
            spawner.spawnParticles();
        }
    }

    public static void burstSquare(Level level, Vec3 pos, Element element, int amount, float randomness) {
        burst(level, pos, element, SquareParticles::squareParticle, amount, randomness);
    }

    public static void zap(Level level, Vec3 pos, Element element, int amount, float scale, float length, float complexity, float randomness, int trailLength) {
        for (int i = 0; i < amount; i++) {
            Vec3 end = pos.add(VectorHelper.random(level.random).scale(length));
            TrailVFX.directionalZapTrail(level, pos, end, scale, complexity, randomness, trailLength, element);
        }
    }

    public static void spray(Level level, Vec3 pos, Element element, Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier, Vec3 direction, int amount, float speed, float randomness) {
        for (int i = 0; i < amount; i++) {
            Vec3 motion = VectorHelper.randScaledRandom(level.random).scale(randomness).add(direction.scale(speed));
            ParticleEffectSpawner spawner = supplier.apply(level, pos, element);
            PresetHelper.modify(spawner, builder -> builder.setMotion(motion));
            spawner.spawnParticles();
        }
    }

    public static void spraySquare(Level level, Vec3 pos, Element element, Vec3 direction, int amount, float speed, float randomness) {
        spray(level, pos, element, SquareParticles::squareParticle, direction, amount, speed, randomness);
    }

    public static void ring(Level level, Vec3 pos, Element element, Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier, Vec3 direction, int amount, float speed, float radius, float randomness) {
        Vec3 normalizedDirection = direction.normalize();

        // directionに垂直な2つの軸を計算
        Vec3 perpendicular1;
        if (Math.abs(normalizedDirection.x) < 0.9) {
            perpendicular1 = new Vec3(0, -normalizedDirection.z, normalizedDirection.y).normalize();
        } else {
            perpendicular1 = new Vec3(-normalizedDirection.y, normalizedDirection.x, 0).normalize();
        }
        Vec3 perpendicular2 = normalizedDirection.cross(perpendicular1).normalize();

        for (int i = 0; i < amount; i++) {
            Vec3 randomXZ = VectorHelper.randomXZ(level.random);

            // XZ平面のランダムベクトルをdirectionに垂直な平面に変換
            Vec3 rotatedRandom = perpendicular1.scale(randomXZ.x).add(perpendicular2.scale(randomXZ.z));

            Vec3 motion = VectorHelper.randScaledRandom(level.random).scale(randomness).add(direction.scale(speed));
            ParticleEffectSpawner spawner = supplier.apply(level, pos.add(rotatedRandom.scale(radius)), element);
            PresetHelper.modify(spawner, builder -> builder.setMotion(motion));
            spawner.spawnParticles();
        }
    }

    public static void ringSquare(Level level, Vec3 pos, Element element, Vec3 direction, int amount, float speed, float radius, float randomness) {
        ring(level, pos, element, SquareParticles::squareParticle, direction, amount, speed, radius, randomness);
    }
}
