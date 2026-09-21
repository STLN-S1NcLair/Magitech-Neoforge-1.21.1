package net.stln.magitech.api.machine.inspection.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.feature.element.Element;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.awt.Color;

/**
 * Inspectionのゲージと枠に使う歪み描画を担当します。
 * Owns distorted rendering for inspection gauges and frames.
 */
public final class MachineInspectionDistortedFrameRenderer {
    private static final ResourceLocation INSPECTION_MANA =
            Magitech.id("textures/gui/inspection_mana.png");
    private static final ResourceLocation MANA_DISTORTED =
            Magitech.id("textures/gui/mana_distorted.png");
    private static final ResourceLocation INSPECTION_BORDER =
            Magitech.id("textures/gui/inspection_border.png");
    private static final int INSPECTION_BORDER_TEXTURE_SIZE = 64;
    private static final int INSPECTION_BORDER_TEXTURE_BORDER = 16;
    private static final int INSPECTION_BORDER_TEXTURE_TILE_SIZE = 32;
    private static final float INSPECTION_BORDER_PIXEL_SCALE = 0.5F;
    private static final float INSPECTION_BORDER_DISTORTION_INTENSITY = 120.0F;
    private static final int INSPECTION_BORDER_DISTORTION_MARGIN_MIN = 4;
    private static final int INSPECTION_MANA_TEXTURE_WIDTH = 256;
    private static final int INSPECTION_MANA_TEXTURE_HEIGHT = 32;
    public static final int MANA_DRAW_WIDTH = 128;
    private static final int INSPECTION_MANA_DRAW_WIDTH = MANA_DRAW_WIDTH;
    public static final int MANA_DRAW_HEIGHT = 16;
    private static final int INSPECTION_MANA_DRAW_HEIGHT = MANA_DRAW_HEIGHT;
    private static final int INSPECTION_MANA_DRAW_BORDER = 4;
    private static final int INSPECTION_MANA_DRAW_INNER_HEIGHT =
            INSPECTION_MANA_DRAW_HEIGHT - INSPECTION_MANA_DRAW_BORDER * 2;
    private static final int MANA_DISTORTED_TEXTURE_SIZE = 16;
    private static final int MANA_DISTORTED_FRAME_COUNT = 16;
    private static final float MANA_DISTORTED_FRAME_TIME = 2.0F;
    private static final int MANA_GAUGE_HEIGHT = 8;
    private static final ShaderHolder DISTORTED_INSPECTION_BORDER_SHADER = new ShaderHolder(
            Magitech.id("inspection_border"),
            DefaultVertexFormat.POSITION_TEX_COLOR
    );

    private MachineInspectionDistortedFrameRenderer() {
    }

    public static void registerShaders(RegisterShadersEvent event) {
        DISTORTED_INSPECTION_BORDER_SHADER.register(event);
    }

    /**
     * inspection_mana のフレームと mana_distorted のアニメーションでマナゲージを描画します。
     * Renders the mana gauge with the inspection_mana frame and animated mana_distorted texture.
     */
    public static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int width,
            int height,
            double ratio
    ) {
        renderManaGauge(
                guiGraphics,
                x,
                y,
                height,
                ratio
        );
    }

    public static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int height,
            double ratio
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        renderManaGauge(
                guiGraphics,
                x,
                y,
                height,
                ratio,
                minecraft.getTimer().getGameTimeDeltaPartialTick(false)
        );
    }

    public static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int height,
            double ratio,
            float partialTick
    ) {
        renderManaGauge(guiGraphics, x, y, height, ratio, partialTick, Element.MANA, 1.0F);
    }

    public static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int height,
            double ratio,
            float partialTick,
            float fadeAlpha
    ) {
        renderManaGauge(guiGraphics, x, y, height, ratio, partialTick, Element.MANA, fadeAlpha);
    }

    public static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int height,
            double ratio,
            float partialTick,
            Element element
    ) {
        renderManaGauge(guiGraphics, x, y, height, ratio, partialTick, element, 1.0F);
    }

    public static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int height,
            double ratio,
            float partialTick,
            Element element,
            float fadeAlpha
    ) {
        double clampedRatio = Math.clamp(ratio, 0.0D, 1.0D);
        int gaugeWidth = INSPECTION_MANA_DRAW_WIDTH;
        int fillHeight = Math.min(MANA_GAUGE_HEIGHT, Math.max(0, height));
        int innerWidth = Math.max(0, gaugeWidth - INSPECTION_MANA_DRAW_BORDER * 2);
        float currentWidth = (float) (innerWidth * clampedRatio);
        renderManaFrame(guiGraphics, x, y, element, fadeAlpha);
        if (currentWidth > 0 && fillHeight > 0) {
            renderDistortedManaFill(
                    guiGraphics,
                    x + INSPECTION_MANA_DRAW_BORDER,
                    y + INSPECTION_MANA_DRAW_BORDER
                            + (INSPECTION_MANA_DRAW_INNER_HEIGHT - fillHeight) / 2,
                    element.getPrimary(),
                    fadeAlpha,
                    currentWidth,
                    fillHeight,
                    partialTick,
                    0.0F
            );
            renderDistortedManaFill(
                    guiGraphics,
                    x + INSPECTION_MANA_DRAW_BORDER,
                    y + INSPECTION_MANA_DRAW_BORDER
                            + (INSPECTION_MANA_DRAW_INNER_HEIGHT - fillHeight) / 2,
                    element.getSecondary(),
                    0.5F * fadeAlpha,
                    currentWidth,
                    fillHeight,
                    partialTick,
                    1000.0F
            );
            renderDistortedManaFill(
                    guiGraphics,
                    x + INSPECTION_MANA_DRAW_BORDER,
                    y + INSPECTION_MANA_DRAW_BORDER
                            + (INSPECTION_MANA_DRAW_INNER_HEIGHT - fillHeight) / 2,
                    element.getDark(),
                    0.5F * fadeAlpha,
                    currentWidth,
                    fillHeight,
                    partialTick,
                    2000.0F
            );
        }
    }

    private static void renderManaFrame(GuiGraphics guiGraphics, int x, int y) {
        renderManaFrame(guiGraphics, x, y, Element.MANA);
    }

    private static void renderManaFrame(GuiGraphics guiGraphics, int x, int y, Element element) {
        renderManaFrame(guiGraphics, x, y, element, 1.0F);
    }

    private static void renderManaFrame(GuiGraphics guiGraphics, int x, int y, Element element, float fadeAlpha) {
        ExtendedShaderInstance shader = DISTORTED_INSPECTION_BORDER_SHADER.getShaderInstance();
        if (shader == null) {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, fadeAlpha);
            try {
                guiGraphics.blit(
                        INSPECTION_MANA,
                        x,
                        y,
                        INSPECTION_MANA_DRAW_WIDTH,
                        INSPECTION_MANA_DRAW_HEIGHT,
                        0,
                        0,
                        INSPECTION_MANA_TEXTURE_WIDTH,
                        INSPECTION_MANA_TEXTURE_HEIGHT,
                        INSPECTION_MANA_TEXTURE_WIDTH,
                        INSPECTION_MANA_TEXTURE_HEIGHT
                );
            } finally {
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
            return;
        }

        int width = INSPECTION_MANA_DRAW_WIDTH;
        int height = INSPECTION_MANA_DRAW_HEIGHT;
        int marginX = Math.max(
                INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                Mth.ceil(width / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
        );
        int marginY = Math.max(
                INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                Mth.ceil(height / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
        );
        float textureBorder = INSPECTION_MANA_DRAW_BORDER / INSPECTION_BORDER_PIXEL_SCALE;
        float tileWidth = INSPECTION_MANA_TEXTURE_WIDTH - textureBorder * 2.0F;
        float tileHeight = INSPECTION_MANA_TEXTURE_HEIGHT - textureBorder * 2.0F;

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFunc(770, 1);
        try {
            renderManaFramePass(
                    guiGraphics,
                    shader,
                    x,
                    y,
                    width,
                    height,
                    marginX,
                    marginY,
                    tileWidth,
                    tileHeight,
                    element.getPrimary(),
                    0.7F * fadeAlpha,
                    0.0F
            );
            renderManaFramePass(
                    guiGraphics,
                    shader,
                    x,
                    y,
                    width,
                    height,
                    marginX,
                    marginY,
                    tileWidth,
                    tileHeight,
                    element.getSecondary(),
                    0.5F * fadeAlpha,
                    1000.0F
            );
            renderManaFramePass(
                    guiGraphics,
                    shader,
                    x,
                    y,
                    width,
                    height,
                    marginX,
                    marginY,
                    tileWidth,
                    tileHeight,
                    element.getDark(),
                    0.5F * fadeAlpha,
                    2000.0F
            );
        } finally {
            shader.setUniformDefaults();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private static void renderManaFramePass(
            GuiGraphics guiGraphics,
            ExtendedShaderInstance shader,
            int x,
            int y,
            int width,
            int height,
            int marginX,
            int marginY,
            float tileWidth,
            float tileHeight,
            Color color,
            float alpha,
            float phaseOffset
    ) {
        setInspectionTextureShaderUniforms(
                shader,
                width,
                height,
                marginX,
                marginY,
                INSPECTION_MANA_TEXTURE_WIDTH,
                INSPECTION_MANA_TEXTURE_HEIGHT,
                INSPECTION_MANA_DRAW_BORDER / INSPECTION_BORDER_PIXEL_SCALE,
                tileWidth,
                tileHeight,
                1.0F,
                0,
                0.0F,
                phaseOffset
        );
        shader.safeGetUniform("Speed").set(420.0F);
        VFXBuilders.createScreen()
                .setShader(shader)
                .setTexture(INSPECTION_MANA)
                .setColor(color, alpha)
                .setUVWithWidth(0.0F, 0.0F, 1.0F, 1.0F)
                .setPositionWithWidth(
                        x - marginX,
                        y - marginY,
                        width + marginX * 2,
                        height + marginY * 2
                )
                .blit(guiGraphics.pose());
        guiGraphics.bufferSource().endBatch();
    }

    private static void renderDistortedManaFill(
            GuiGraphics guiGraphics,
            int x,
            int y,
            Color color,
            float alpha,
            float width,
            int height,
            float partialTick,
            float phaseOffset
    ) {
        ExtendedShaderInstance shader = DISTORTED_INSPECTION_BORDER_SHADER.getShaderInstance();
        if (shader == null || width <= 0 || height <= 0) {
            return;
        }

        int marginX = Math.max(
                INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                Mth.ceil(width / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
        );
        int marginY = Math.max(
                INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                Mth.ceil(height / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
        );
        double animationFrame = getManaAnimationFrame(partialTick);
        long animationFrameIndex = (long) Math.floor(animationFrame);
        int frameIndex = (int) Math.floorMod(
                animationFrameIndex,
                (long) MANA_DISTORTED_FRAME_COUNT
        );
        float frameBlend = (float) (animationFrame - animationFrameIndex);
        setInspectionTextureShaderUniforms(
                shader,
                width,
                height,
                marginX,
                marginY,
                MANA_DISTORTED_TEXTURE_SIZE,
                MANA_DISTORTED_TEXTURE_SIZE,
                0.0F,
                MANA_DISTORTED_TEXTURE_SIZE,
                MANA_DISTORTED_TEXTURE_SIZE,
                MANA_DISTORTED_FRAME_COUNT,
                frameIndex,
                frameBlend,
                phaseOffset
        );
        Minecraft.getInstance().getTextureManager()
                .getTexture(MANA_DISTORTED)
                .setFilter(true, false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        try {
            VFXBuilders.createScreen()
                    .setShader(shader)
                    .setTexture(MANA_DISTORTED)
                    .setColor(color, alpha)
                    .setUVWithWidth(0.0F, 0.0F, 1.0F, 1.0F)
                    .setPositionWithWidth(
                            x - marginX,
                            y - marginY,
                            width + marginX * 2,
                            height + marginY * 2
                    )
                    .blit(guiGraphics.pose());
            guiGraphics.bufferSource().endBatch();
        } finally {
            shader.setUniformDefaults();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
        }
    }

    /**
     * 後方互換用に色引数を受け取るマナゲージ描画メソッドです。色はテクスチャとエフェクトで決まります。
     * Compatibility overload accepting a color argument; the textures and effects determine the displayed color.
     */
    public static void renderManaGauge(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int width,
            int height,
            double ratio,
            int ignoredFillColor
    ) {
        renderManaGauge(guiGraphics, x, y, width, height, ratio);
    }

    private static double getManaAnimationFrame(float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return 0.0D;
        }
        return (minecraft.level.getGameTime() + (double) partialTick)
                / MANA_DISTORTED_FRAME_TIME;
    }

    public static void renderBorder(
            GuiGraphics guiGraphics,
            int x,
            int y,
            int width,
            int height,
            float fadeAlpha
    ) {
        ExtendedShaderInstance shader = DISTORTED_INSPECTION_BORDER_SHADER.getShaderInstance();
        if (shader == null) {
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFunc(770, 1);
        try {
            int marginX = Math.max(
                    INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                    Mth.ceil(width / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
            );
            int marginY = Math.max(
                    INSPECTION_BORDER_DISTORTION_MARGIN_MIN,
                    Mth.ceil(height / INSPECTION_BORDER_DISTORTION_INTENSITY) + 1
            );
            setInspectionBorderShaderUniforms(shader, width, height, marginX, marginY, 0.0F);
            VFXBuilders.createScreen()
                    .setShader(shader)
                    .setTexture(INSPECTION_BORDER)
                    .setColor(Element.MANA.getPrimary(), 0.7F * fadeAlpha)
                    .setUVWithWidth(0.0F, 0.0F, 1.0F, 1.0F)
                    .setPositionWithWidth(x - marginX, y - marginY, width + marginX * 2, height + marginY * 2)
                    .blit(guiGraphics.pose());
            guiGraphics.bufferSource().endBatch();

            setInspectionBorderShaderUniforms(shader, width, height, marginX, marginY, 1000.0F);
            VFXBuilders.createScreen()
                    .setShader(shader)
                    .setTexture(INSPECTION_BORDER)
                    .setColor(Element.MANA.getSecondary(), 0.5F * fadeAlpha)
                    .setUVWithWidth(0.0F, 0.0F, 1.0F, 1.0F)
                    .setPositionWithWidth(x - marginX, y - marginY, width + marginX * 2, height + marginY * 2)
                    .blit(guiGraphics.pose());
            guiGraphics.bufferSource().endBatch();

            setInspectionBorderShaderUniforms(shader, width, height, marginX, marginY, 2000.0F);
            VFXBuilders.createScreen()
                    .setShader(shader)
                    .setTexture(INSPECTION_BORDER)
                    .setColor(Element.MANA.getDark(), 0.5F * fadeAlpha)
                    .setUVWithWidth(0.0F, 0.0F, 1.0F, 1.0F)
                    .setPositionWithWidth(x - marginX, y - marginY, width + marginX * 2, height + marginY * 2)
                    .blit(guiGraphics.pose());
            guiGraphics.bufferSource().endBatch();
        } finally {
            shader.setUniformDefaults();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
        }
    }

    private static void setInspectionBorderShaderUniforms(
            ExtendedShaderInstance shader,
            int width,
            int height,
            int marginX,
            int marginY,
            float phaseOffset
    ) {
        setInspectionTextureShaderUniforms(
                shader,
                width,
                height,
                marginX,
                marginY,
                INSPECTION_BORDER_TEXTURE_SIZE,
                INSPECTION_BORDER_TEXTURE_SIZE,
                INSPECTION_BORDER_TEXTURE_BORDER,
                INSPECTION_BORDER_TEXTURE_TILE_SIZE,
                INSPECTION_BORDER_TEXTURE_TILE_SIZE,
                1.0F,
                0.0F,
                0.0F,
                phaseOffset
        );
    }

    private static void setInspectionTextureShaderUniforms(
            ExtendedShaderInstance shader,
            float width,
            int height,
            int marginX,
            int marginY,
            float textureWidth,
            float textureHeight,
            float borderSize,
            float tileWidth,
            float tileHeight,
            float frameCount,
            float frameIndex,
            float frameBlend,
            float phaseOffset
    ) {
        shader.safeGetUniform("Width").set((float) width);
        shader.safeGetUniform("Height").set((float) height);
        shader.safeGetUniform("MarginX").set((float) marginX);
        shader.safeGetUniform("MarginY").set((float) marginY);
        shader.safeGetUniform("BorderSize").set(borderSize);
        shader.safeGetUniform("TileWidth").set(tileWidth);
        shader.safeGetUniform("TileHeight").set(tileHeight);
        shader.safeGetUniform("TextureSize").set(textureWidth);
        shader.safeGetUniform("TextureWidth").set(textureWidth);
        shader.safeGetUniform("TextureHeight").set(textureHeight);
        shader.safeGetUniform("PixelScale").set(INSPECTION_BORDER_PIXEL_SCALE);
        shader.safeGetUniform("Speed").set(420.0F);
        shader.safeGetUniform("Intensity").set(INSPECTION_BORDER_DISTORTION_INTENSITY);
        shader.safeGetUniform("YFrequency").set(5.0F);
        shader.safeGetUniform("XFrequency").set(5.0F);
        shader.safeGetUniform("FrameCount").set(frameCount);
        shader.safeGetUniform("FrameIndex").set(frameIndex);
        shader.safeGetUniform("FrameBlend").set(frameBlend);
        shader.safeGetUniform("TimeOffset").set(phaseOffset);
    }
}
