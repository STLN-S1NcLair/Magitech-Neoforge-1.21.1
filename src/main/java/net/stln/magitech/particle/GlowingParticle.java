package net.stln.magitech.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;

public class GlowingParticle extends AbstractCustomizableParticle {

    protected float scale = 0.1F;

    protected GlowingParticle(ClientLevel world, double x, double y, double z) {
        super(world, x, y, z);
    }

    protected GlowingParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
    }

    @Override
    protected int getLightColor(float tint) {
        if (this.age >= this.lifetime * 0.8F) {
            return (int) (((this.lifetime - this.age) / (this.lifetime * 0.2F) * 0.6F + 0.2F) * 240);
        } else {
            return 240;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return AbstractCustomizableParticle.PARTICLE_SHEET_ADDITIVE;
    }
}
