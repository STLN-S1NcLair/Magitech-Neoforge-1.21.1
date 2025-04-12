package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.WaveParticleEffect;

public class WaveParticleType extends ParticleType<WaveParticleEffect> {
    public WaveParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<WaveParticleEffect> codec() {
        return WaveParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, WaveParticleEffect> streamCodec() {
        return WaveParticleEffect.STREAM_CODEC;
    }
}
