package net.stln.magitech.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.stln.magitech.particle.particle_option.SquareFieldParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class SquareFieldParticle extends HorizontalParticle {

    private final SpriteSet spriteProvider;
    private final Vector3f startColor;
    private final Vector3f endColor;

    public SquareFieldParticle(ClientLevel clientWorld, double x, double y, double z, double vx, double vy, double vz,
                               SquareFieldParticleEffect parameters, SpriteSet spriteProvider) {
        super(clientWorld, x, y, z, vx, vy, vz);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.lifetime = parameters.getLifetime();
        this.alpha = 1.0F;
        this.quadSize = 0.2F;
        this.scale = 4F * parameters.getScale();
        this.friction = parameters.getFriction();
        this.gravity = -0.0F;
        this.spriteProvider = spriteProvider;
        this.setSpriteFromAge(spriteProvider);
        this.startColor = parameters.getFromColor();
        this.endColor = parameters.getToColor();
        this.twinkle = parameters.getTwinkle();
        this.rotSpeed = parameters.getRotSpeed();
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        float f = ((float) this.age + tickDelta) / ((float) this.lifetime + 1.0F);
        Vector3f vector3f = new Vector3f(this.startColor).lerp(this.endColor, f);
        this.rCol = vector3f.x();
        this.gCol = vector3f.y();
        this.bCol = vector3f.z();

        if (this.age >= this.lifetime * 0.8F) {
            this.alpha = (this.lifetime - this.age) / (this.lifetime * 0.2F) * 0.6F + 0.2F;
        }
        if (this.twinkle > 1) {
            float multiplier = Math.max(((float) this.age % this.twinkle) / (this.twinkle - 1), 0.1F);
            this.rCol *= multiplier;
            this.gCol *= multiplier;
            this.bCol *= multiplier;
        }

        super.render(vertexConsumer, camera, tickDelta);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.move(this.xd, this.yd, this.zd);
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd = this.yd - 0.04 * (double) this.gravity;
        }

        this.xd = this.xd * (double) this.friction;
        this.yd = this.yd * (double) this.friction;
        this.zd = this.zd * (double) this.friction;

//        rotate();

        this.setSpriteFromAge(this.spriteProvider);
    }

    @Override
    protected int getLightColor(float tint) {
        if (this.age >= this.lifetime * 0.8F) {
            return (int) (((this.lifetime - this.age) / (this.lifetime * 0.2F) * 0.6F + 0.2F) * 240);
        } else {
            return 240;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SquareFieldParticleEffect> {
        private final SpriteSet spriteProvider;

        public Provider(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SquareFieldParticleEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new SquareFieldParticle(world, x, y, z, velocityX, velocityY, velocityZ, parameters, this.spriteProvider);
        }
    }
}
