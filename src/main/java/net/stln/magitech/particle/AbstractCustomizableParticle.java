package net.stln.magitech.particle;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;

public class AbstractCustomizableParticle extends TextureSheetParticle {

    public static ParticleRenderType PARTICLE_SHEET_ADDITIVE = new ParticleRenderType() {
        @Override
        public BufferBuilder begin(Tesselator p_350826_, TextureManager p_107456_) {
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            return p_350826_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT";
        }
    };
    protected int twinkle;
    protected float defaultAlpha;

    protected AbstractCustomizableParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
    }

    protected AbstractCustomizableParticle(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    protected float darken(float colorComponent, float multiplier) {
        return (this.random.nextFloat() * 0.2F + 0.8F) * colorComponent * multiplier;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return PARTICLE_SHEET_ADDITIVE;
    }
}
