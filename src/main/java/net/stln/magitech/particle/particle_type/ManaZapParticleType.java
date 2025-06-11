package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.ManaZapParticleEffect;

public class ManaZapParticleType extends ParticleType<ManaZapParticleEffect> {
    public ManaZapParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<ManaZapParticleEffect> codec() {
        return ManaZapParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ManaZapParticleEffect> streamCodec() {
        return ManaZapParticleEffect.STREAM_CODEC;
    }
}
