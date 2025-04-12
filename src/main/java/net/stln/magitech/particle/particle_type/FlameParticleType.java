package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.FlameParticleEffect;
import net.stln.magitech.particle.particle_option.FrostParticleEffect;

public class FlameParticleType extends ParticleType<FlameParticleEffect> {
    public FlameParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<FlameParticleEffect> codec() {
        return FlameParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, FlameParticleEffect> streamCodec() {
        return FlameParticleEffect.STREAM_CODEC;
    }
}
