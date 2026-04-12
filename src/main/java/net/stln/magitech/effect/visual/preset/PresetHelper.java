package net.stln.magitech.effect.visual.preset;

import team.lodestar.lodestone.systems.particle.ParticleEffectSpawner;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;

import java.util.function.Consumer;

public class PresetHelper {

    public static ParticleEffectSpawner modify(ParticleEffectSpawner spawner, Consumer<WorldParticleBuilder> consumer) {
        consumer.accept(spawner.getBuilder());
        if (spawner.getBloomBuilder() != null) {
            consumer.accept(spawner.getBloomBuilder());
        }
        return spawner;
    }

    public static ParticleEffectSpawner modifyBloomTransparency(ParticleEffectSpawner spawner, float value) {
        spawner.getBloomBuilder().modifyTransparencyData(genericParticleData -> genericParticleData.multiplyValue(value));
        return spawner;
    }

    public static ParticleEffectSpawner bigger(ParticleEffectSpawner spawner, float value) {
        return modify(spawner, builder -> builder.modifyScaleData(data -> data.multiplyValue(value)));
    }

    public static ParticleEffectSpawner bigger(ParticleEffectSpawner spawner) {
        return bigger(spawner, 2.0F);
    }

    public static ParticleEffectSpawner smaller(ParticleEffectSpawner spawner) {
        return bigger(spawner, 0.5F);
    }

    public static ParticleEffectSpawner longer(ParticleEffectSpawner spawner, float value) {
        return modify(spawner, builder -> builder.modifyLifetime(data -> (int) (data * value)));
    }

    public static ParticleEffectSpawner longer(ParticleEffectSpawner spawner) {
        return longer(spawner, 2.0F);
    }

    public static ParticleEffectSpawner friction(ParticleEffectSpawner spawner, float friction) {
        return modify(spawner, builder -> builder.setFriction(friction));
    }
}
