package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.SquareParticleEffect;

public class SquareParticleType extends ParticleType<SquareParticleEffect> {
    public SquareParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<SquareParticleEffect> codec() {
        return SquareParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, SquareParticleEffect> streamCodec() {
        return SquareParticleEffect.STREAM_CODEC;
    }
}
