package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.MembraneParticleEffect;

public class MembraneParticleType extends ParticleType<MembraneParticleEffect> {
    public MembraneParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<MembraneParticleEffect> codec() {
        return MembraneParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, MembraneParticleEffect> streamCodec() {
        return MembraneParticleEffect.STREAM_CODEC;
    }
}
