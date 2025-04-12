package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.RuneParticleEffect;

public class RuneParticleType extends ParticleType<RuneParticleEffect> {
    public RuneParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<RuneParticleEffect> codec() {
        return RuneParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, RuneParticleEffect> streamCodec() {
        return RuneParticleEffect.STREAM_CODEC;
    }
}
