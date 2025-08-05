package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.SquareNoCullParticleEffect;

public class SquareNoCullParticleType extends ParticleType<SquareNoCullParticleEffect> {
    public SquareNoCullParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<SquareNoCullParticleEffect> codec() {
        return SquareNoCullParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, SquareNoCullParticleEffect> streamCodec() {
        return SquareNoCullParticleEffect.STREAM_CODEC;
    }
}
