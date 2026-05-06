package net.stln.magitech.effect.visual.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.effect.visual.particle.particle_type.*;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.util.function.Supplier;

public class ParticleInit {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Magitech.MOD_ID);

    public static final Supplier<BeamParticleType> BEAM = PARTICLE_TYPES.register("beam", () -> new BeamParticleType(true));

    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> LD_SQUARE = PARTICLE_TYPES.register("square", LodestoneWorldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> RING = PARTICLE_TYPES.register("ring", LodestoneWorldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> RING_REVERSED = PARTICLE_TYPES.register("ring_reversed", LodestoneWorldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> LD_POWERUP = PARTICLE_TYPES.register("powerup", LodestoneWorldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> RAY = PARTICLE_TYPES.register("ray", LodestoneWorldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> SMOKE = PARTICLE_TYPES.register("smoke", LodestoneWorldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> SNOW = PARTICLE_TYPES.register("snow", LodestoneWorldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> GLINT = PARTICLE_TYPES.register("glint", LodestoneWorldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> LD_RUNE = PARTICLE_TYPES.register("rune", LodestoneWorldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> LEAF = PARTICLE_TYPES.register("leaf", LodestoneWorldParticleType::new);
    public static final DeferredHolder<ParticleType<?>, LodestoneWorldParticleType> RIFT = PARTICLE_TYPES.register("rift", LodestoneWorldParticleType::new);

    public static void registerParticleType(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Client Particle for " + Magitech.MOD_ID);
        PARTICLE_TYPES.register(eventBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BEAM.get(), BeamParticle.Provider::new);
        event.registerSpriteSet(LD_SQUARE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(RING.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(RING_REVERSED.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LD_POWERUP.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(RAY.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(SMOKE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(SNOW.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(GLINT.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LD_RUNE.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(LEAF.get(), LodestoneWorldParticleType.Factory::new);
        event.registerSpriteSet(RIFT.get(), LodestoneWorldParticleType.Factory::new);
    }
}
