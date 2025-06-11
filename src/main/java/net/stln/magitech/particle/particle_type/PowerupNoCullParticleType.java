package net.stln.magitech.particle.particle_type;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.particle.particle_option.PowerupNoCullParticleEffect;

public class PowerupNoCullParticleType extends ParticleType<PowerupNoCullParticleEffect> {
    public PowerupNoCullParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<PowerupNoCullParticleEffect> codec() {
        return PowerupNoCullParticleEffect.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, PowerupNoCullParticleEffect> streamCodec() {
        return PowerupNoCullParticleEffect.STREAM_CODEC;
    }
}
