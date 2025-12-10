package net.stln.magitech.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.stln.magitech.particle.particle_option.WaveParticleEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class WaveParticle extends GlowingParticle {

    private final SpriteSet spriteProvider;
    private final Vector3f startColor;
    private final Vector3f endColor;

    public WaveParticle(ClientLevel clientWorld, double x, double y, double z, double vx, double vy, double vz,
                        WaveParticleEffect parameters, SpriteSet spriteProvider) {
        super(clientWorld, x, y, z, vx, vy, vz);
        this.xd = vx + (clientWorld.random.nextFloat() - 0.5F) / 30;
        this.yd = vy + (clientWorld.random.nextFloat() - 0.5F) / 30;
        this.zd = vz + (clientWorld.random.nextFloat() - 0.5F) / 30;
        this.lifetime = parameters.getLifetime();
        this.alpha = 1.0F;
        this.scale = 1F * parameters.getScale();
        this.gravity = 0.0F;
        this.friction = parameters.getFriction();
        this.spriteProvider = spriteProvider;
        this.setSpriteFromAge(spriteProvider);
        this.startColor = parameters.getFromColor();
        this.endColor = parameters.getToColor();
        this.twinkle = parameters.getTwinkle();
        this.rotSpeed = parameters.getRotSpeed() + (clientWorld.random.nextFloat() - 0.5F) / 10;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        this.updateColor(tickDelta);
        if (this.twinkle > 1) {
            float multiplier = Math.max(((float) this.age % this.twinkle) / (this.twinkle - 1), 0.1F);
            this.rCol *= multiplier;
            this.gCol *= multiplier;
            this.bCol *= multiplier;
        }

        super.render(vertexConsumer, camera, tickDelta);
    }

    private void updateColor(float tickDelta) {
        float f = ((float) this.age + tickDelta) / ((float) this.lifetime + 1.0F);
        Vector3f vector3f = new Vector3f(this.startColor).lerp(this.endColor, f);
        this.rCol = vector3f.x();
        this.gCol = vector3f.y();
        this.bCol = vector3f.z();
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

        rotate();

        this.setSpriteFromAge(this.spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<WaveParticleEffect> {
        private final SpriteSet spriteProvider;

        public Provider(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(WaveParticleEffect parameters, ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
            return new WaveParticle(world, x, y, z, xd, yd, zd, parameters, this.spriteProvider);
        }
    }
}
