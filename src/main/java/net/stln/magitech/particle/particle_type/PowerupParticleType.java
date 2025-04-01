package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.PowerupParticleEffect;

public class PowerupParticleType extends ParticleType<PowerupParticleEffect> {
    public PowerupParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<PowerupParticleEffect> codec() {
        return PowerupParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, PowerupParticleEffect> streamCodec() {
        return PowerupParticleEffect.STREAM_CODEC;
    }
}
