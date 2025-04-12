package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.VoidGlowParticleEffect;

public class VoidGlowParticleType extends ParticleType<VoidGlowParticleEffect> {
    public VoidGlowParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<VoidGlowParticleEffect> codec() {
        return VoidGlowParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, VoidGlowParticleEffect> streamCodec() {
        return VoidGlowParticleEffect.STREAM_CODEC;
    }
}
