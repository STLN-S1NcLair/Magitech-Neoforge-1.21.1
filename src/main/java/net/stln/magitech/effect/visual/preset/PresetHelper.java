package net.stln.magitech.effect.visual.preset;

import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;

import java.util.function.Consumer;
import java.util.function.Function;

public class PresetHelper {

    public static ParticleEffectSpawner modify(ParticleEffectSpawner spawner, Consumer<WorldParticleBuilder> consumer) {
        consumer.accept(spawner.getBuilder());
        if (spawner.getBloomBuilder() != null) {
            consumer.accept(spawner.getBloomBuilder());
        }
        return spawner;
    }
}
