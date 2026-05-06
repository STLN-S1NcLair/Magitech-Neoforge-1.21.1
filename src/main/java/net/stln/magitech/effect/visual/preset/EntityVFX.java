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
import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;

import java.awt.*;

public class EntityVFX {

    public static void powerupAura(Level level, Element element, Entity entity, Section heightSection, float amount) {
        powerupAura(level, element.getPrimary(), element.getSecondary(), entity, heightSection, amount);
    }

    public static void powerupAura(Level level, Element element, Entity entity, float amount) {
        powerupAura(level, element, entity, Section.firstHalf(), amount);
    }

    public static void powerupAura(Level level, Trait trait, Entity entity, Section heightSection, float amount) {
        powerupAura(level, trait.getPrimary(), trait.getSecondary(), entity, heightSection, amount);
    }

    public static void powerupAura(Level level, Trait trait, Entity entity, float amount) {
        powerupAura(level, trait, entity, Section.firstHalf(), amount);
    }

    public static void powerupAura(Level level, Color primary, Color secondary, Entity entity, Section heightSection, float amount) {
        for (int i = 0; i < amount; i++) {
            if (i != Mth.floor(amount) || level.random.nextFloat() < amount - i) {
                Vec3 randomBody = EffectHelper.getRandomBody(entity, heightSection);
                ParticleEffectSpawner spawner = PowerupParticles.powerupParticle(level, randomBody, primary, secondary);
                PresetHelper.modify(spawner, builder -> {
                    builder.addTickActor(BehaviorPreset.toDestination(entity, 0.995F, 0.0F, 1.0F, false));
                });
                spawner.spawnParticles();
            }
        }
    }
}
