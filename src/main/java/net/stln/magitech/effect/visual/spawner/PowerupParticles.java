package net.stln.magitech.effect.visual.spawner;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.particle.ParticleInit;
import net.stln.magitech.effect.visual.preset.BehaviorPreset;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.feature.element.Element;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import java.awt.*;
import java.util.function.Consumer;

public class PowerupParticles {

    public static ParticleEffectSpawner powerupParticle(Level level, Vec3 pos, Color primary, Color secondary, float acceleration) {
        RandomSource random = level.getRandom();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.LD_POWERUP);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        ColorParticleData colorParticleData = ColorParticleData.create(primary, secondary).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(primary, secondary).build();
        Consumer<LodestoneWorldParticle> behavior = (particle) -> {
            Vec3 particleSpeed = particle.getParticleSpeed();
            if (particleSpeed.y > 0) {
                particle.setParticleSpeed(particleSpeed.add(0, -Math.min(acceleration * 0.05F, particleSpeed.y), 0));
            }
        };
        bloomColorParticleData.multiplyCoefficient(1.5F);
        int lifetime = random.nextInt(10, 50);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.05F, Mth.randomBetween(random, 0.9F, 1.1F), 0.9F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(1.0F, 0.5F))
                .setFriction(0.99F)
                .addMotion(0, acceleration, 0)
                .addTickActor(behavior)
                .setForceSpawn(true)
                .enableNoClip()
                .setLifetime(lifetime);
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.05F, Mth.randomBetween(random, 0.2F, 0.3F), 0.3F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 0F))
                .setFriction(0.99F)
                .addMotion(0, acceleration, 0)
                .addTickActor(behavior)
                .setForceSpawn(true)
                .enableNoClip()
                .setLifetime(lifetime);
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }

    public static ParticleEffectSpawner powerupParticle(Level level, Vec3 pos, Color primary, Color secondary) {
        return powerupParticle(level, pos, primary, secondary, 0.08F);
    }
}
