package net.stln.magitech.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.AABB;

public class AbstractCustomizableParticle extends TextureSheetParticle {
    protected float scale;
    protected float rotSpeed;
    protected int twinkle;
    protected boolean cull = true;
    protected float defaultAlpha;

    protected AbstractCustomizableParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
    }

    protected AbstractCustomizableParticle(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    protected void rotate() {
        this.oRoll = this.roll;
        this.roll += this.rotSpeed;
        this.rotSpeed *= this.friction;
    }

    @Override
    public AABB getRenderBoundingBox(float partialTicks) {
        if (this.cull) {
            return super.getRenderBoundingBox(partialTicks);
        }
        return AABB.INFINITE;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.quadSize * this.scale;
    }

    protected float darken(float colorComponent, float multiplier) {
        return (this.random.nextFloat() * 0.2F + 0.8F) * colorComponent * multiplier;
    }

    @Override
    public ParticleRenderType getRenderType() {
        if (this.cull) {
            return CustomParticleRenderTypes.PARTICLE_SHEET_ADDITIVE;
        }
        return CustomParticleRenderTypes.PARTICLE_SHEET_ADDITIVE_NO_CULL;
    }
}
