package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.BeamParticleEffect;

public class BeamParticleType extends ParticleType<BeamParticleEffect> {
    public BeamParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<BeamParticleEffect> codec() {
        return BeamParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, BeamParticleEffect> streamCodec() {
        return BeamParticleEffect.STREAM_CODEC;
    }
}
