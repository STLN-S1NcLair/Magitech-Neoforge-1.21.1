package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.FrostParticleEffect;

public class FrostParticleType extends ParticleType<FrostParticleEffect> {
    public FrostParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<FrostParticleEffect> codec() {
        return FrostParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FrostParticleEffect> streamCodec() {
        return FrostParticleEffect.STREAM_CODEC;
    }
}
