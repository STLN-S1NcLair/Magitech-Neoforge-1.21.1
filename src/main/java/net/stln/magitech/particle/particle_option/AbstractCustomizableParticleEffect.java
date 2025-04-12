package net.stln.magitech.particle.particle_option;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.particles.ParticleOptions;
import net.stln.magitech.util.MathHelper;
import org.joml.Vector3f;

public abstract class AbstractCustomizableParticleEffect implements ParticleOptions {
    public static final float MIN_SCALE = 0.01F;
    public static final float MAX_SCALE = 10.0F;
    protected static final Codec<Float> SCALE_CODEC = Codec.FLOAT
            .validate(scale -> scale >= 0.01F && scale <= 10.0F ? DataResult.success(scale) : DataResult.error(() -> "Value must be within range [0.01;10.0]: " + scale));
    protected static final Codec<Integer> TWINKLE_CODEC = Codec.INT
            .validate(twinkle -> twinkle >= 1 && twinkle <= 100 ? DataResult.success(twinkle) : DataResult.error(() -> "Value must be within range [1;100]: " + twinkle));
    protected static final Codec<Float> ROTATE_SPEED_CODEC = Codec.FLOAT
            .validate(rotSpeed -> rotSpeed >= -100 && rotSpeed <= 100 ? DataResult.success(rotSpeed) : DataResult.error(() -> "Value must be within range [-100;100]: " + rotSpeed));
    private final Vector3f color;
    private final float scale;
    private final int twinkle;
    private final float rotSpeed;

    public AbstractCustomizableParticleEffect(Vector3f color, float scale, int twinkle, float rotSpeed) {
        this.color = color;
        this.scale = MathHelper.clamp(scale, 0.01F, 4.0F);
        this.twinkle = twinkle;
        this.rotSpeed = rotSpeed;
    }

    public AbstractCustomizableParticleEffect(float scale, int twinkle, float rotSpeed) {
        this.scale = MathHelper.clamp(scale, 0.01F, 10.0F);
        this.twinkle = MathHelper.clamp(twinkle, 1, 100);
        this.rotSpeed = MathHelper.clamp(rotSpeed, -100, 100);
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
}
