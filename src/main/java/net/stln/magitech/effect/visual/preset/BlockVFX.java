package net.stln.magitech.effect.visual.preset;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.effect.visual.Section;
import net.stln.magitech.effect.visual.spawner.PowerupParticles;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.trait.Trait;
import net.stln.magitech.helper.EffectHelper;
import net.stln.magitech.helper.VectorHelper;
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;

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
}
