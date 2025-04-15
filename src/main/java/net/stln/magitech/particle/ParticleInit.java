package net.stln.magitech.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.stln.magitech.Magitech;
import net.stln.magitech.particle.particle_option.FlameSmokeParticleEffect;
import net.stln.magitech.particle.particle_option.ZapParticleEffect;
import net.stln.magitech.particle.particle_type.*;

import java.util.function.Supplier;

public class ParticleInit {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Magitech.MOD_ID);

    public static final Supplier<SquareFieldParticleType> SQUARE_FIELD = PARTICLE_TYPES.register("square_field", () -> new SquareFieldParticleType(true));
    public static final Supplier<UnstableSquareParticleType> UNSTABLE_SQUARE = PARTICLE_TYPES.register("unstable_square", () -> new UnstableSquareParticleType(true));
    public static final Supplier<FlameParticleType> FLAME = PARTICLE_TYPES.register("flame", () -> new FlameParticleType(true));
    public static final Supplier<FlameSmokeParticleType> FLAME_SMOKE = PARTICLE_TYPES.register("flame_smoke", () -> new FlameSmokeParticleType(true));
    public static final Supplier<FrostParticleType> FROST = PARTICLE_TYPES.register("frost", () -> new FrostParticleType(true));
    public static final Supplier<SparkParticleType> SPARK = PARTICLE_TYPES.register("spark", () -> new SparkParticleType(true));
    public static final Supplier<MembraneParticleType> MEMBRANE = PARTICLE_TYPES.register("membrane", () -> new MembraneParticleType(true));
    public static final Supplier<WaveParticleType> WAVE = PARTICLE_TYPES.register("wave", () -> new WaveParticleType(true));
    public static final Supplier<RuneParticleType> RUNE = PARTICLE_TYPES.register("rune", () -> new RuneParticleType(true));
    public static final Supplier<BlowParticleType> BLOW = PARTICLE_TYPES.register("blow", () -> new BlowParticleType(true));
    public static final Supplier<VoidGlowParticleType> VOID_GLOW = PARTICLE_TYPES.register("void_glow", () -> new VoidGlowParticleType(true));
    public static final Supplier<PowerupParticleType> POWERUP = PARTICLE_TYPES.register("powerup", () -> new PowerupParticleType(true));
    public static final Supplier<ZapParticleType> ZAP = PARTICLE_TYPES.register("zap", () -> new ZapParticleType(true));

    @Environment(EnvType.CLIENT)
    public static void registerParticleClient(IEventBus eventBus) {
        Magitech.LOGGER.info("Registering Client Particle for " + Magitech.MOD_ID);
        PARTICLE_TYPES.register(eventBus);
    }

    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SQUARE_FIELD.get(), SquareFieldParticle.Provider::new);
        event.registerSpriteSet(UNSTABLE_SQUARE.get(), UnstableSquareParticle.Provider::new);
        event.registerSpriteSet(FLAME.get(), FlameParticle.Provider::new);
        event.registerSpriteSet(FLAME_SMOKE.get(), FlameSmokeParticle.Provider::new);
        event.registerSpriteSet(FROST.get(), FrostParticle.Provider::new);
        event.registerSpriteSet(SPARK.get(), SparkParticle.Provider::new);
        event.registerSpriteSet(MEMBRANE.get(), MembraneParticle.Provider::new);
        event.registerSpriteSet(WAVE.get(), WaveParticle.Provider::new);
        event.registerSpriteSet(RUNE.get(), RuneParticle.Provider::new);
        event.registerSpriteSet(BLOW.get(), BlowParticle.Provider::new);
        event.registerSpriteSet(VOID_GLOW.get(), VoidGlowParticle.Provider::new);
        event.registerSpriteSet(POWERUP.get(), PowerupParticle.Provider::new);
        event.registerSpriteSet(ZAP.get(), ZapParticle.Provider::new);
    }
}
