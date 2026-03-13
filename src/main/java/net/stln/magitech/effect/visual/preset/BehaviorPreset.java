package net.stln.magitech.effect.visual.preset;

import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.util.function.Consumer;

public class BehaviorPreset {

    public static Consumer<LodestoneWorldParticle> toDestination(Vec3 pos) {
        return toDestination(pos, 1.0F);
    }


    public static Consumer<LodestoneWorldParticle> toDestination(Vec3 pos, float acceleration, float ageCoefficient, float strength) {
        return particle -> {
            Vec3 to = pos.subtract(particle.getParticlePosition()).normalize();
            double length = particle.getParticleSpeed().length();
            float delta = 1 - ageCoefficient + ageCoefficient * (particle.getAge() / (float) particle.getLifetime());
            delta *= strength;
            particle.setParticleSpeed(particle.getParticleSpeed().lerp(to.scale((length - 1) * acceleration + 1), delta));
        };
    }


    public static Consumer<LodestoneWorldParticle> toDestination(Vec3 pos, float acceleration) {
        return toDestination(pos, acceleration, 0.8F, 1.0F);
    }

    public static Consumer<LodestoneWorldParticle> gravity(float acceleration) {
        return gravity(new Vec3(0, -1, 0), acceleration);
    }


    public static Consumer<LodestoneWorldParticle> gravity(Vec3 direction, float acceleration) {
        return particle -> {
            float progress = (float) particle.getAge() / particle.getLifetime();
            particle.setParticleSpeed(particle.getParticleSpeed().add(direction.scale(acceleration * progress * progress)));
        };
    }
}
