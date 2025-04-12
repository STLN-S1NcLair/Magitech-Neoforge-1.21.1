package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.BlowParticleEffect;

public class BlowParticleType extends ParticleType<BlowParticleEffect> {
    public BlowParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<BlowParticleEffect> codec() {
        return BlowParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, BlowParticleEffect> streamCodec() {
        return BlowParticleEffect.STREAM_CODEC;
    }
}
