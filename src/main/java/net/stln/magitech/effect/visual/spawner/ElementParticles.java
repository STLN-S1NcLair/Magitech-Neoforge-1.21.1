package net.stln.magitech.effect.visual.spawner;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.particle.ParticleInit;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.helper.ColorHelper;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
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
        Color primary = ColorHelper.Argb.mul(ColorHelper.Argb.dilute(element.getColor()), SMOKE_COLOR);
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
                .setLifetime(lifetime);
        return new ParticleEffectSpawner(level, pos, builder);
    }

    public static ParticleEffectSpawner snowParticle(Level level, Vec3 pos, Element element) {
        RandomSource random = level.getRandom();
        SpinParticleData spinParticleData = SpinParticleData.createRandomDirection(random, Mth.nextFloat(random, 0F, 0.1F)).randomSpinOffset(random).build();
        WorldParticleOptions options = new WorldParticleOptions(ParticleInit.SNOW);
        WorldParticleOptions bloomOptions = new WorldParticleOptions(LodestoneParticleTypes.WISP_PARTICLE);
        Color primary = ColorHelper.Argb.dilute(element.getColor());
        Color secondary = ColorHelper.Argb.dilute(element.getSecondary());
        ColorParticleData colorParticleData = ColorParticleData.create(primary, secondary).build();
        ColorParticleData bloomColorParticleData = ColorParticleData.create(element.getColor(), element.getDark()).build();
        int lifetime = random.nextInt(30, 80);
        WorldParticleBuilder builder = WorldParticleBuilder.create(options)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.1F, 0.3F), 0.1F))
                .setColorData(colorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 1.0F, 0.5F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setLifetime(lifetime)
                .setGravity(0.2F);
        WorldParticleBuilder bloomBuilder = WorldParticleBuilder.create(bloomOptions)
                .setScaleData(GenericParticleData.create(0.1F, Mth.randomBetween(random, 0.2F, 0.3F), 0.1F))
                .setColorData(bloomColorParticleData)
                .setTransparencyData(GenericParticleData.create(0.5F, 0F))
                .setFriction(0.95F)
                .setSpinData(spinParticleData)
                .setLifetime(lifetime)
                .setGravity(0.2F);
        return new ParticleEffectSpawner(level, pos, builder, bloomBuilder);
    }
}
