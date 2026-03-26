package net.stln.magitech.effect.visual.preset;

import com.mojang.datafixers.util.Function3;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.VectorHelper;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.util.function.Consumer;


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

    public static void spreadLinedSquare(Level level, Vec3 start, Vec3 end, Element element, Section section, float density, float speed, float randomness) {
        spreadLined(level, start, end, element, SquareParticles::squareParticle, section, density, speed, randomness);
    }

    public static void destinationLinedSquare(Level level, Vec3 start, Vec3 end, Element element, Section section, float density, float speed, float randomness) {
        destinationLined(level, start, end, element, SquareParticles::squareParticle, section, density, speed, randomness);
    }
}
