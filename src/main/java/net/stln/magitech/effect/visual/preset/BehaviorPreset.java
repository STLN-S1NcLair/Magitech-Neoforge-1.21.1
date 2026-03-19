package net.stln.magitech.effect.visual.preset;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.helper.CombatHelper;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.TickScheduler;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.util.Map;
import java.util.function.Consumer;

public class BehaviorPreset {

    public static Consumer<LodestoneWorldParticle> toDestination(Vec3 pos) {
        return toDestination(pos, 1.0F);
    }


    // acceleration: 0 -> 目的までの距離に応じて加速, 1 -> 加速せずに目的に移動
    public static Consumer<LodestoneWorldParticle> toDestination(Vec3 pos, float acceleration) {
        return toDestination(pos, acceleration, 0.8F, 1.0F);
    }


    // ageCoefficient: 0 -> age関係なく移動, 1 -> ageが大きくなると移動
    public static Consumer<LodestoneWorldParticle> toDestination(Vec3 pos, float acceleration, float ageCoefficient, float strength) {
        return particle -> {
            Vec3 to = pos.subtract(particle.getParticlePosition()).normalize();
            double length = particle.getParticleSpeed().length();
            float delta = 1 - ageCoefficient + ageCoefficient * (particle.getAge() / (float) particle.getLifetime());
            delta *= strength;
            particle.setParticleSpeed(particle.getParticleSpeed().lerp(to.normalize().scale((length - 1) * acceleration + 1), delta));
        };
    }

    public static Consumer<LodestoneWorldParticle> toDestination(Entity pos) {
        return toDestination(pos, 1.0F);
    }


    public static Consumer<LodestoneWorldParticle> toDestination(Entity pos, float acceleration) {
        return toDestination(pos, acceleration, 0.8F, 1.0F, false);
    }


    public static Consumer<LodestoneWorldParticle> toDestination(Entity pos, float acceleration, float ageCoefficient, float strength, boolean vortex) {
        return toDestination(pos, acceleration, ageCoefficient, (pos.getBbWidth() + pos.getBbHeight()) * 0.5F * Mth.sqrt(2), strength, vortex);
    }


    // disableRadius: 移動を無効化する半径
    // vortex: 近づいても減速しないか?
    public static Consumer<LodestoneWorldParticle> toDestination(Entity pos, float acceleration, float ageCoefficient, float disableRadius, float strength, boolean vortex) {
        return particle -> {
            Vec3 subtract = CombatHelper.getBodyPos(pos).subtract(particle.getParticlePosition());
            if (subtract.length() > disableRadius) {
                Vec3 to = subtract.normalize();
                double length = particle.getParticleSpeed().length();
                float delta = 1 - ageCoefficient + ageCoefficient * (particle.getAge() / (float) particle.getLifetime());
                delta *= strength;
                float speed = !vortex ? (float) Math.min(Math.pow(subtract.length(), 2), 1.0F) : 1.0F;
                particle.setParticleSpeed(particle.getParticleSpeed().lerp(to.scale((length - 1) * acceleration + 1), delta).scale(speed));
            }
        };
    }


    public static Consumer<LodestoneWorldParticle> follow(Entity entity, float strength) {
        return particle -> {
            Vec3 move = entity.position().subtract(new Vec3(entity.xOld, entity.yOld, entity.zOld));
            particle.setParticleSpeed(particle.getParticleSpeed().add(move.scale(strength)));
            TickScheduler.schedule(1, () -> {
                particle.setParticleSpeed(particle.getParticleSpeed().subtract(move.scale(strength)));
            }, entity.level().isClientSide);
        };
    }


    public static Consumer<LodestoneWorldParticle> follow(Entity entity) {
        return follow(entity, 1.0F);
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
