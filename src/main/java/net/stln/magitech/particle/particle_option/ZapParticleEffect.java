package net.stln.magitech.particle.particle_option;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.stln.magitech.particle.ParticleInit;
import org.joml.Vector3f;

public class ZapParticleEffect extends AbstractCustomizableParticleEffect {
    public static final MapCodec<ZapParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ExtraCodecs.VECTOR3F.fieldOf("from_color").forGetter(effect -> effect.fromColor),
                            ExtraCodecs.VECTOR3F.fieldOf("to_color").forGetter(effect -> effect.toColor),
                            ExtraCodecs.VECTOR3F.fieldOf("to_pos").forGetter(effect -> effect.toPos),
                            SCALE_CODEC.fieldOf("scale").forGetter(AbstractCustomizableParticleEffect::getScale),
                            TWINKLE_CODEC.fieldOf("twinkle").forGetter(AbstractCustomizableParticleEffect::getTwinkle),
                            ROTATE_SPEED_CODEC.fieldOf("rotate_speed").forGetter(AbstractCustomizableParticleEffect::getRotSpeed),
                            LIFETIME_CODEC.fieldOf("lifetime").forGetter(AbstractCustomizableParticleEffect::getLifetime),
                            FRICTION_CODEC.fieldOf("friction").forGetter(AbstractCustomizableParticleEffect::getFriction)
                    )
                    .apply(instance, ZapParticleEffect::new)
    );
    public static final StreamCodec<ByteBuf, ZapParticleEffect> STREAM_CODEC = composite(
            ByteBufCodecs.VECTOR3F,
            effect -> effect.fromColor,
            ByteBufCodecs.VECTOR3F,
            effect -> effect.toColor,
            ByteBufCodecs.VECTOR3F,
            effect -> effect.toPos,
            ByteBufCodecs.FLOAT,
            AbstractCustomizableParticleEffect::getScale,
            ByteBufCodecs.INT,
            AbstractCustomizableParticleEffect::getTwinkle,
            ByteBufCodecs.FLOAT,
            AbstractCustomizableParticleEffect::getRotSpeed,
            ByteBufCodecs.INT,
            AbstractCustomizableParticleEffect::getLifetime,
            ByteBufCodecs.FLOAT,
            AbstractCustomizableParticleEffect::getFriction,
            ZapParticleEffect::new
    );

    private final Vector3f fromColor;
    private final Vector3f toColor;
    private final Vector3f toPos;

    public ZapParticleEffect(Vector3f fromColor, Vector3f toColor, Vector3f toPos, float scale, int twinkle, float rotSpeed, int lifetime, float friction) {
        super(scale, twinkle, rotSpeed, lifetime, friction);
        this.fromColor = fromColor;
        this.toColor = toColor;
        this.toPos = toPos;
    }


    public Vector3f getFromColor() {
        return this.fromColor;
    }

    public Vector3f getToColor() {
        return this.toColor;
    }

    public Vector3f getToPos() {
        return this.toPos;
    }

    @Override
    public ParticleType<ZapParticleEffect> getType() {
        return ParticleInit.ZAP.get();
    }
}
