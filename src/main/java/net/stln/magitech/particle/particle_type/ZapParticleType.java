package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.ZapParticleEffect;

public class ZapParticleType extends ParticleType<ZapParticleEffect> {
    public ZapParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<ZapParticleEffect> codec() {
        return ZapParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ZapParticleEffect> streamCodec() {
        return ZapParticleEffect.STREAM_CODEC;
    }
}
