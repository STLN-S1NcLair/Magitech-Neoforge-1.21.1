package net.stln.magitech.particle.particle_option;

import com.mojang.datafixers.util.Function6;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function8;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.codec.StreamCodec;
import net.stln.magitech.util.MathHelper;
import org.joml.Vector3f;

import java.util.function.Function;

public abstract class AbstractCustomizableParticleEffect implements ParticleOptions {
    public static final float MIN_SCALE = 0.01F;
    public static final float MAX_SCALE = 10.0F;
    protected static final Codec<Float> SCALE_CODEC = Codec.FLOAT
            .validate(scale -> scale >= 0.01F && scale <= 10.0F ? DataResult.success(scale) : DataResult.error(() -> "Value must be within range [0.01;10.0]: " + scale));
    protected static final Codec<Integer> TWINKLE_CODEC = Codec.INT
            .validate(twinkle -> twinkle >= 1 && twinkle <= 100 ? DataResult.success(twinkle) : DataResult.error(() -> "Value must be within range [1;100]: " + twinkle));
    protected static final Codec<Float> ROTATE_SPEED_CODEC = Codec.FLOAT
            .validate(rotSpeed -> rotSpeed >= -100 && rotSpeed <= 100 ? DataResult.success(rotSpeed) : DataResult.error(() -> "Value must be within range [-100;100]: " + rotSpeed));
    protected static final Codec<Integer> LIFETIME_CODEC = Codec.INT
            .validate(lifetime -> lifetime >= 1 && lifetime <= 1000 ? DataResult.success(lifetime) : DataResult.error(() -> "Value must be within range [1;1000]: " + lifetime));
    protected static final Codec<Float> FRICTION_CODEC = Codec.FLOAT
            .validate(friction -> friction >= 0 && friction <= 1 ? DataResult.success(friction) : DataResult.error(() -> "Value must be within range [0;1]: " + friction));
    private final Vector3f color;
    private final float scale;
    private final int twinkle;
    private final float rotSpeed;
    private final int lifetime;
    private final float friction;

    public AbstractCustomizableParticleEffect(Vector3f color, float scale, int twinkle, float rotSpeed, boolean cull, int lifetime, float friction) {
        this.color = color;
        this.scale = MathHelper.clamp(scale, 0.01F, 10.0F);
        this.twinkle = twinkle;
        this.rotSpeed = rotSpeed;
        this.lifetime = lifetime;
        this.friction = friction;
    }

    public AbstractCustomizableParticleEffect(Vector3f color, float scale, int twinkle, float rotSpeed, int lifetime, float friction) {
        this.color = color;
        this.scale = MathHelper.clamp(scale, 0.01F, 10.0F);
        this.twinkle = twinkle;
        this.rotSpeed = rotSpeed;
        this.lifetime = lifetime;
        this.friction = friction;
    }

    public AbstractCustomizableParticleEffect(float scale, int twinkle, float rotSpeed, boolean cull, int lifetime, float friction) {
        this.scale = MathHelper.clamp(scale, 0.01F, 10.0F);
        this.twinkle = MathHelper.clamp(twinkle, 1, 100);
        this.rotSpeed = MathHelper.clamp(rotSpeed, -100, 100);
        this.lifetime = MathHelper.clamp(lifetime, 1, 1000);
        this.friction = MathHelper.clamp(friction, 0, 1);
        this.color = new Vector3f(0.0F, 0.0F, 0.0F);
    }

    public AbstractCustomizableParticleEffect(float scale, int twinkle, float rotSpeed, int lifetime, float friction) {
        this.scale = MathHelper.clamp(scale, 0.01F, 10.0F);
        this.twinkle = MathHelper.clamp(twinkle, 1, 100);
        this.rotSpeed = MathHelper.clamp(rotSpeed, -100, 100);
        this.lifetime = MathHelper.clamp(lifetime, 1, 1000);
        this.friction = MathHelper.clamp(friction, 0, 1);
        this.color = new Vector3f(0.0F, 0.0F, 0.0F);
    }

    public float getScale() {
        return this.scale;
    }

    public int getTwinkle() {
        return this.twinkle;
    }

    public float getRotSpeed() {
        return rotSpeed;
    }

    public int getLifetime() {
        return lifetime;
    }

    public float getFriction() {
        return friction;
    }


    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> factory
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = codec1.decode(p_330310_);
                T2 t2 = codec2.decode(p_330310_);
                T3 t3 = codec3.decode(p_330310_);
                T4 t4 = codec4.decode(p_330310_);
                T5 t5 = codec5.decode(p_330310_);
                T6 t6 = codec6.decode(p_330310_);
                T7 t7 = codec7.decode(p_330310_);
                T8 t8 = codec8.decode(p_330310_);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            @Override
            public void encode(B p_332052_, C p_331912_) {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
                codec7.encode(p_332052_, getter7.apply(p_331912_));
                codec8.encode(p_332052_, getter8.apply(p_331912_));
            }
        };
    }

    static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final Function7<T1, T2, T3, T4, T5, T6, T7, C> factory
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = codec1.decode(p_330310_);
                T2 t2 = codec2.decode(p_330310_);
                T3 t3 = codec3.decode(p_330310_);
                T4 t4 = codec4.decode(p_330310_);
                T5 t5 = codec5.decode(p_330310_);
                T6 t6 = codec6.decode(p_330310_);
                T7 t7 = codec7.decode(p_330310_);
                return factory.apply(t1, t2, t3, t4, t5, t6, t7);
            }

            @Override
            public void encode(B p_332052_, C p_331912_) {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
                codec7.encode(p_332052_, getter7.apply(p_331912_));
            }
        };
    }

}
