package net.stln.magitech.effect.visual.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

public class CustomParticleRenderTypes {
    public static final ParticleRenderType PARTICLE_SHEET_ADDITIVE = new ParticleRenderType() {
        @Override
        public BufferBuilder begin(Tesselator p_350826_, TextureManager p_107456_) {
            // 深度テストは有効にしておきつつ、深度バッファへの書き込みを無効化することで
            // 前面のパーティクルによる奥の描画ブロックを防ぎ、重なりを正しくブレンドする
            RenderSystem.depthMask(false);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE.value);
            return p_350826_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public boolean isTranslucent() {
            return true;
        }

        @Override
        public String toString() {
            return "magitech:additive";
        }
    };

    public static final ParticleRenderType PARTICLE_SHEET_ADDITIVE_NO_CULL = new ParticleRenderType() {
        @Override
        public BufferBuilder begin(Tesselator p_350826_, TextureManager p_107456_) {
            // 深度テストを無効にするノーカル向け。こちらも深度書き込みは行わない
            RenderSystem.depthMask(false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE.value);
            RenderSystem.disableDepthTest();

            return p_350826_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public boolean isTranslucent() {
            return true;
        }

        @Override
        public String toString() {
            return "magitech:additive_no_cull";
        }
    };
}
