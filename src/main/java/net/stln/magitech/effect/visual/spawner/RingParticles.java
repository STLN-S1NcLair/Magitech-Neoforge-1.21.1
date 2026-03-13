package net.stln.magitech.effect.visual.spawner;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.particle.ParticleInit;
import net.stln.magitech.effect.visual.preset.PresetHelper;
import net.stln.magitech.feature.element.Element;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.behaviors.DirectionalParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

public class RingParticles {

    private static ParticleEffectSpawner ring(Level level, Vec3 pos, WorldParticleOptions options, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.1F)).randomSpinOffset(random).build();
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        ColorParticleData colorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        bloomColorParticleData.multiplyCoefficient(1.5F);
        int lifetime = random.nextInt(10, 20);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.5F, Mth.randomBetween(random, 0.8F, 1.0F), 0.5F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(1.0F, 1.0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime)
                .enableNoClip();
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.7F, Mth.randomBetween(random, 1.0F, 1.2F), 0.5F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime)
                .enableNoClip();
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }

    public static ParticleEffectSpawner ringParticle(Level level, Vec3 pos, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.1F)).randomSpinOffset(random).build();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.RING);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        ColorParticleData colorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        bloomColorParticleData.multiplyCoefficient(1.5F);
        int lifetime = random.nextInt(10, 20);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.5F, Mth.randomBetween(random, 0.8F, 1.0F), 0.5F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(1.0F, 1.0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime)
                .enableNoClip();
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.7F, Mth.randomBetween(random, 1.0F, 1.2F), 0.5F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime)
                .enableNoClip();
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }

    public static ParticleEffectSpawner ringParticle(Level level, Vec3 pos, Vec3 direction, Element element) {
        ParticleEffectSpawner spawner = ringParticle(level, pos, element);
        PresetHelper.modify(spawner, builder -> builder.setBehavior(DirectionalParticleBehavior.directional(direction)));
        return spawner;
    }

    public static ParticleEffectSpawner ringReversedParticle(Level level, Vec3 pos, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.1F)).randomSpinOffset(random).build();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.RING_REVERSED);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        ColorParticleData colorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        bloomColorParticleData.multiplyCoefficient(1.5F);
        int lifetime = random.nextInt(10, 20);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.5F, Mth.randomBetween(random, 0.8F, 1.0F), 0.5F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(1.0F, 1.0F, 0.0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime)
                .enableNoClip();
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.5F, Mth.randomBetween(random, 1.0F, 1.2F), 0.0F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0F, 0.5F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime)
                .enableNoClip();
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }

    public static ParticleEffectSpawner ringReversedParticle(Level level, Vec3 pos, Vec3 direction, Element element) {
        ParticleEffectSpawner spawner = ringReversedParticle(level, pos, element);
        PresetHelper.modify(spawner, builder -> builder.setBehavior(DirectionalParticleBehavior.directional(direction)));
        return spawner;
    }

}
