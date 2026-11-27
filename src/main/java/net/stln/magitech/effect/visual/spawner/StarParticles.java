package net.stln.magitech.effect.visual.spawner;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.particle.ParticleInit;
import net.stln.magitech.feature.element.Element;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import java.awt.*;

public class StarParticles {

    public static ParticleEffectSpawner starParticle(Level level, Vec3 pos, Element element) {
        return starParticleColored(level, pos, element.getPrimary(), element.getSecondary());
    }

    public static ParticleEffectSpawner starParticleColored(Level level, Vec3 pos, Color primary, Color secondary) {
        RandomSource random = level.getRandom();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.STAR);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        ColorParticleData colorParticleData = ColorParticleData.create(primary, secondary).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(primary, secondary).build();
        bloomColorParticleData.multiplyCoefficient(1.5F);
        int lifetime = random.nextInt(40, 44);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.4F, 0.1F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.5F, 0.6F), 0.0F))
                .setFriction(0.95F)
                .setForceSpawn(true)
                .setLifetime(lifetime);
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0F, 0.2F, 0F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.01F, 0.1F, 0F))
                .setFriction(0.95F)
                .setForceSpawn(true)
                .setLifetime(lifetime);
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }

}
