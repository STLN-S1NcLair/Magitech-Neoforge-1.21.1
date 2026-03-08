package net.stln.magitech.effect.visual.preset;

import com.mojang.datafixers.util.Function3;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.VectorHelper;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.util.function.Consumer;

public class PointVFX {
    public static void vortex(Level level, Vec3 pos, Element element, Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier, int amount, float acceleration, float randomness) {
        for (int i = 0; i < amount; i++) {
            Vec3 motion = VectorHelper.random().scale(randomness);
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
            Vec3 motion = VectorHelper.random().scale(randomness);
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
            Vec3 end = pos.add(VectorHelper.random().scale(length));
            TrailVFX.zapTrail(level, pos, end, scale, complexity, randomness, trailLength, element);
        }
    }
}
