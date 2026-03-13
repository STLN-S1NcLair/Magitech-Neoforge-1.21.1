package net.stln.magitech.effect.visual.spawner;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.particle.ParticleInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.ColorHelper;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import java.awt.*;

public class ElementParticles {

    public static final Color SMOKE_COLOR = new Color(0x404040);
    public static final Color SMOKE_SECONDARY = new Color(0x202020);

    public static ParticleEffectSpawner smokeParticle(Level level, Vec3 pos, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.1F)).randomSpinOffset(random).build();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.SMOKE);
        Color primary = ColorHelper.Argb.mul(ColorHelper.Argb.dilute(element.getPrimary()), SMOKE_COLOR);
        Color secondary = ColorHelper.Argb.mul(ColorHelper.Argb.dilute(element.getSecondary()), SMOKE_SECONDARY);
        ColorParticleData colorParticleData = ColorParticleData.create(primary, secondary).build();
        int lifetime = random.nextInt(30, 80);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setRenderType(LodestoneWorldParticleRenderType.LUMITRANSPARENT)
                .setScaleData(GenericParticleData.create(0.3F, Mth.randomBetween(random, 0.3F, 0.5F), 0.3F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(0F, 1.0F, 0.5F))
                .setFriction(0.9F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime);
        return new ParticleEffectSpawner(level, pos, builder);
    }

    public static ParticleEffectSpawner snowParticle(Level level, Vec3 pos, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.1F)).randomSpinOffset(random).build();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.SNOW);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        Color primary = ColorHelper.Argb.dilute(element.getPrimary());
        Color secondary = ColorHelper.Argb.dilute(element.getSecondary());
        ColorParticleData colorParticleData = ColorParticleData.create(primary, secondary).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        int lifetime = random.nextInt(30, 80);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.1F, 0.3F), 0.1F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 1.0F, 0.5F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setLifetime(lifetime)
                .setForceSpawn(true)
                .setGravity(0.2F);
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.05F, Mth.randomBetween(random, 0.2F, 0.3F), 0.1F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setLifetime(lifetime)
                .setForceSpawn(true)
                .setGravity(0.2F);
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }

    public static ParticleEffectSpawner glintParticle(Level level, Vec3 pos, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.1F)).randomSpinOffset(random).build();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.GLINT);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        Color primary = ColorHelper.Argb.dilute(element.getPrimary());
        Color secondary = element.getPrimary();
        ColorParticleData colorParticleData = ColorParticleData.create(primary, secondary).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        int lifetime = random.nextInt(5, 30);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.1F, 0.3F), 0.1F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 1.0F, 0.5F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime);
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.2F, 0.3F), 0.2F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.7F, 0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime);
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }

    public static ParticleEffectSpawner runeParticle(Level level, Vec3 pos, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.1F)).randomSpinOffset(random).build();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.LD_RUNE);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        ColorParticleData colorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        int lifetime = random.nextInt(5, 60);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.1F, 0.3F), 0.1F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 1.0F, 0.5F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime);
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.2F, 0.3F), 0.2F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime);
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }

    public static ParticleEffectSpawner leafParticle(Level level, Vec3 pos, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.3F)).randomSpinOffset(random).setEasing(Easing.CUBIC_OUT).build();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.LEAF);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        ColorParticleData colorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        int lifetime = random.nextInt(5, 60);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.1F, 0.3F), 0.1F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 1.0F, 0.5F))
                .setFriction(0.93F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime)
                .setGravity(0.3F);
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.2F, 0.3F), 0.2F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 0F))
                .setFriction(0.93F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime)
                .setGravity(0.3F);
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }

    public static ParticleEffectSpawner riftParticle(Level level, Vec3 pos, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.1F)).randomSpinOffset(random).build();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.RIFT);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        ColorParticleData colorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(element.getPrimary(), element.getSecondary()).build();
        int lifetime = random.nextInt(5, 40);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.1F, 0.3F), 0.1F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 1.0F, 0.5F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime);
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.2F, 0.3F), 0.2F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setForceSpawn(true)
                .setLifetime(lifetime);
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }
}
