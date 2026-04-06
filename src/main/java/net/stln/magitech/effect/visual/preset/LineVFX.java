package net.stln.magitech.effect.visual.preset;

import com.mojang.datafixers.util.Function3;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.TrailRenderHelper;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.effect.visual.trail.TrailData;
import net.stln.magitech.effect.visual.trail.TrailRenderer;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.VectorHelper;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


public class LineVFX {

    // speed: start -> endを正とする方向の速度
    public static void spreadLined(Level level, Vec3 start, Vec3 end, Element element, Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier, Section section, float density, float speed, float randomness) {
        double dist = start.distanceTo(end);
        int amount = Mth.ceil(density * dist * section.ratio()) + 1;
        Vec3 direction = end.subtract(start).normalize();
        for (int i = 0; i < amount; i++) {
            double progress = section.ratio() * i / amount + section.start();
            Vec3 motion = direction.scale(speed).add(VectorHelper.random(level.random).scale(randomness));
            Vec3 lerped = start.lerp(end, progress);
            ParticleEffectSpawner spawner = supplier.apply(level, lerped, element);
            PresetHelper.modify(spawner, builder -> builder.setMotion(motion));
            spawner.spawnParticles();
        }
    }

    public static void spreadLinedSquare(Level level, Vec3 start, Vec3 end, Element element, Section section, float density, float speed, float randomness) {
        spreadLined(level, start, end, element, SquareParticles::squareParticle, section, density, speed, randomness);
    }

    public static void destinationLined(Level level, Vec3 start, Vec3 end, Element element, Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier, Section section, float density, float speed, float randomness) {
        double dist = start.distanceTo(end);
        int amount = Mth.ceil(density * dist * section.ratio()) + 1;
        Vec3 direction = end.subtract(start).normalize();
        for (int i = 0; i < amount; i++) {
            double progress = section.ratio() * i / amount + section.start();
            Vec3 motion = direction.scale(speed).add(VectorHelper.random(level.random).scale(randomness));
            Consumer<LodestoneWorldParticle> behavior = BehaviorPreset.toDestination(end, 1.0F);
            Vec3 lerped = start.lerp(end, progress);
            ParticleEffectSpawner spawner = supplier.apply(level, lerped, element);
            PresetHelper.modify(spawner, builder -> builder.setMotion(motion).addTickActor(behavior));
            spawner.spawnParticles();
        }
    }

    public static void destinationLinedSquare(Level level, Vec3 start, Vec3 end, Element element, Section section, float density, float speed, float randomness) {
        destinationLined(level, start, end, element, SquareParticles::squareParticle, section, density, speed, randomness);
    }

    public static void arcLined(Level level, Vec3 center, Vec2 normal, Element element, Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier, float startDeg, float endDeg, float slopeDeg, float radius, float density, float speed, float randomness) {

        int amount = (int) (density * radius * Math.abs(endDeg - startDeg) / 50);

        Vec3 lookVec = Vec3.directionFromRotation(normal); // プレイヤーの視線方向
        double yawRad = org.joml.Math.toRadians(normal.y);

        // **視線方向に基づく「右方向ベクトル」を計算**
        Vec3 rightVec = new Vec3(org.joml.Math.cos(yawRad), 0, org.joml.Math.sin(yawRad)).normalize(); // 視線の右方向
        Vec3 upVec = lookVec.cross(rightVec).normalize(); // 視線に対する上方向

        for (int i = 0; i <= amount; i++) {
            double progress = (double) i / (amount - 1);
            double angleDeg = startDeg + (endDeg - startDeg) * progress;
            double toDeg = startDeg + (endDeg - startDeg) * (progress + 0.01);

            Vec3 axisVec = VectorHelper.rotateVector(upVec, lookVec, slopeDeg);
            Vec3 offset = VectorHelper.rotateVector(lookVec, axisVec, angleDeg);
            Vec3 to = VectorHelper.rotateVector(lookVec, axisVec, toDeg);
            Vec3 direction = offset.subtract(to);

            Vec3 lerped = center.subtract(offset.scale(radius));
            ParticleEffectSpawner spawner = supplier.apply(level, lerped, element);
            Vec3 motion = direction.normalize().scale(speed).add(VectorHelper.random(level.random).scale(randomness));
            PresetHelper.modify(spawner, builder -> builder.setMotion(motion));
            spawner.spawnParticles();
        }
    }

    public static void arcLinedSquare(Level level, Vec3 center, Vec2 normal, Element element, float startDeg, float endDeg, float slopeDeg, float radius, float density, float speed, float randomness) {
        arcLined(level, center, normal, element, SquareParticles::squareParticle, startDeg, endDeg, slopeDeg, radius, density, speed, randomness);
    }
}
