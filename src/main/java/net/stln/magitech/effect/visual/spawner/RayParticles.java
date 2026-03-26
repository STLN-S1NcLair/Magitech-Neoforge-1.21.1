package net.stln.magitech.effect.visual.spawner;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.particle.ParticleInit;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import java.awt.*;

public class RayParticles {

    public static ParticleEffectSpawner rayParticle(Level level, Vec3 pos, Color primary, Color secondary, float width, float height, int lifetime) {
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.RAY);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(ParticleInit.RAY);
        ColorParticleData colorParticleData = ColorParticleData.create(primary, secondary).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(primary, secondary).build();
        bloomColorParticleData.multiplyCoefficient(1.5F);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setLengthData(GenericParticleData.create(0.5F, 0.5F, 0.5F).build().multiplyValue(height))
                .setScaleData(GenericParticleData.create(width))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 1.0F, 0.0F))
                .setFriction(1.0F)
                .setForceSpawn(true)
                .enableNoClip()
                .setLifetime(lifetime);
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setLengthData(GenericParticleData.create(1.0F, 1.0F, 0.5F).build().multiplyValue(height))
                .setScaleData(GenericParticleData.create(width))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.0F, 0.5F, 0F))
                .setFriction(1.0F)
                .setForceSpawn(true)
                .enableNoClip()
                .setLifetime(lifetime);
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }
}
