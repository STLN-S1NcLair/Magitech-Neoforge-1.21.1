package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.FrostShortParticleEffect;

public class FrostShortParticleType extends ParticleType<FrostShortParticleEffect> {
    public FrostShortParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<FrostShortParticleEffect> codec() {
        return FrostShortParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FrostShortParticleEffect> streamCodec() {
        return FrostShortParticleEffect.STREAM_CODEC;
    }
}
