package net.stln.magitech.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.particle.particle_type.FrostParticleType;
import net.stln.magitech.particle.particle_type.PowerupParticleType;
import net.stln.magitech.particle.particle_type.SquareFieldParticleType;
import net.stln.magitech.particle.particle_type.UnstableSquareParticleType;

import java.util.function.Supplier;

public class ParticleInit {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Magitech.MOD_ID);

    public static final Supplier<SquareFieldParticleType> SQUARE_FIELD = PARTICLE_TYPES.register("square_field", () -> new SquareFieldParticleType(true));
    public static final Supplier<UnstableSquareParticleType> UNSTABLE_SQUARE = PARTICLE_TYPES.register("unstable_square", () -> new UnstableSquareParticleType(true));
    public static final Supplier<FrostParticleType> FROST = PARTICLE_TYPES.register("frost", () -> new FrostParticleType(true));;
    public static final Supplier<PowerupParticleType> POWERUP = PARTICLE_TYPES.register("powerup", () -> new PowerupParticleType(true));

    @Environment(EnvType.CLIENT)
    public static void registerParticleClient(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Client Particle for " + Magitech.MOD_ID);
        PARTICLE_TYPES.register(eventBus);
    }

    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SQUARE_FIELD.get(), SquareFieldParticle.Provider::new);
        event.registerSpriteSet(UNSTABLE_SQUARE.get(), UnstableSquareParticle.Provider::new);
        event.registerSpriteSet(FROST.get(), FrostParticle.Provider::new);
        event.registerSpriteSet(POWERUP.get(), PowerupParticle.Provider::new);
    }
}
