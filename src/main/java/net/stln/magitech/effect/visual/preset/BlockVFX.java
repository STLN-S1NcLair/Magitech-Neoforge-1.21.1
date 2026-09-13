package net.stln.magitech.effect.visual.preset;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.spawner.PowerupParticles;
import net.stln.magitech.effect.visual.spawner.SquareParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.trait.Trait;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.VectorHelper;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;

import java.awt.*;

public class BlockVFX {

    public static void traitBreak(Level level, Trait trait, Vec3 pos, float amount) {
        traitBreak(level, trait.getPrimary(), trait.getSecondary(), pos, amount);
    }

    public static void traitBreak(Level level, Color primary, Color secondary, Vec3 pos, float amount) {
        if (!level.isClientSide) return;
        for (int i = 0; i < amount; i++) {
            if (i != Mth.floor(amount) || level.random.nextFloat() < amount - i) {
                Vec3 random = pos.add(VectorHelper.randomInCube(level.random));
                ParticleEffectSpawner spawner = PowerupParticles.powerupParticle(level, random, primary, secondary);
                spawner.spawnParticles();
            }
        }
    }

    public static void fieldEffect(Level level, Color primary, Color secondary, BlockPos pos, float amount) {
        if (!level.isClientSide) return;
        for (int i = 0; i < amount; i++) {
            if (i != Mth.floor(amount) || level.random.nextFloat() < amount - i) {
                Vec3 random = pos.getCenter().add(VectorHelper.randomInCube(level.random));
                ParticleEffectSpawner spawner = SquareParticles.squareParticleColored(level, random, primary, secondary);
                PresetHelper.smaller(spawner);
                PresetHelper.longer(spawner, 8.0F);
                spawner.spawnParticles();
            }
        }
    }

    public static void horizontalSpray(Level level, Color primary, Color secondary, Vec3 pos, float amount, float speed, float gravity, float radius) {
        horizontalSpray(level, primary, secondary, pos, SquareParticles::squareParticleColored, amount, speed, gravity, radius);
    }

    public static void horizontalSpray(Level level, Element element, Vec3 pos, Function3<Level, Vec3, Element, ParticleEffectSpawner> supplier, float amount, float speed, float gravity, float radius) {
        horizontalSpray(level, element.getPrimary(), element.getSecondary(), pos, (l, p, primary, secondary) -> supplier.apply(l, p, element), amount, speed, gravity, radius);
    }

    public static void horizontalSpray(Level level, Color primary, Color secondary, Vec3 pos, Function4<Level, Vec3, Color, Color, ParticleEffectSpawner> supplier, float amount, float speed, float gravity, float radius) {
        if (!level.isClientSide) return;
        for (int i = 0; i < amount; i++) {
            if (i != Mth.floor(amount) || level.random.nextFloat() < amount - i) {
                Vec3 random = VectorHelper.randomXZ(level.random);
                ParticleEffectSpawner spawner = supplier.apply(level, pos.add(random.scale(radius)), primary, secondary);
                PresetHelper.modify(spawner, builder -> builder.setMotion(random.scale(speed)));
                PresetHelper.modify(spawner, WorldParticleBuilder::enableNoClip);
                PresetHelper.modify(spawner, builder -> builder.setGravity(gravity));
                spawner.spawnParticles();
            }
        }
    }
}
