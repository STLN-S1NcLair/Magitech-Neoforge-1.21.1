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
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

public class SquareParticles {

    public static ParticleEffectSpawner squareParticle(Level level, Vec3 pos, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.1F)).randomSpinOffset(random).build();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.LD_SQUARE);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        ColorParticleData colorParticleData = ColorParticleData.create(element.getColor(), element.getSecondary()).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(element.getColor(), element.getDark()).build();
        bloomColorParticleData.multiplyCoefficient(1.5F);
        int lifetime = random.nextInt(10, 20);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.03F, Mth.randomBetween(random, 0.2F, 0.3F), 0F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(1.0F, 0.2F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setLifetime(lifetime)
                .enableNoClip();
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.01F, Mth.randomBetween(random, 0.2F, 0.3F), 0F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setLifetime(lifetime)
                .enableNoClip();
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }

    public static ParticleEffectSpawner squareGravityParticle(Level level, Vec3 pos, Element element, float acceleration) {
        ParticleEffectSpawner spawner = squareParticle(level, pos, element);
        PresetHelper.modify(spawner, builder -> builder.addTickActor(BehaviorPreset.gravity(acceleration)).disableNoClip());
        return spawner;
    }

    public static ParticleEffectSpawner squareGravityParticle(Level level, Vec3 pos, Element element) {
        return squareGravityParticle(level, pos, element, 0.5F);
    }

    public static ParticleEffectSpawner squareFloatingParticle(Level level, Vec3 pos, Element element) {
        return squareGravityParticle(level, pos, element, -0.2F);
    }

}
