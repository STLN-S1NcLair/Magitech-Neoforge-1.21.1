package net.stln.magitech.api.machine.inspection.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.stln.magitech.Magitech;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.rendering.LodestoneBufferWrapper;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeProvider;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.util.function.Function;

 /**
  * Inspectionの文字に使う歪み描画を担当します。
  * Owns distorted rendering for inspection text.
  */
public final class MachineInspectionDistortedTextRenderer {
    private static final float TEXT_DISTORTION_SPEED = 2400F;
    private static final float TEXT_DISTORTION_AMPLITUDE = 1.25F;
    private static final ShaderHolder VERTEX_DISTORTED_TEXT_SHADER = new ShaderHolder(
            Magitech.id("inspection_text"),
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP
    );
    private static final RenderTypeProvider DISTORTED_TEXT = new RenderTypeProvider(
            token -> LodestoneRenderTypes.createAdditiveRenderType(
                    "magitech_distorted_text",
                    token,
                    VERTEX_DISTORTED_TEXT_SHADER
            )
    );
    private static final Function<GuiGraphics, LodestoneBufferWrapper> ADDITIVE_TEXT_BUFFER =
            Util.memoize(guiGraphics -> new LodestoneBufferWrapper(LodestoneRenderTypes.ADDITIVE_TEXT, guiGraphics.bufferSource()));
    private static final Function<GuiGraphics, LodestoneBufferWrapper> DISTORTED_TEXT_BUFFER =
            Util.memoize(guiGraphics -> new LodestoneBufferWrapper(DISTORTED_TEXT, guiGraphics.bufferSource()));

    private MachineInspectionDistortedTextRenderer() {
    }

    public static void registerShaders(RegisterShadersEvent event) {
        VERTEX_DISTORTED_TEXT_SHADER.register(event);
    }

    public static void draw(
            GuiGraphics guiGraphics,
            Minecraft minecraft,
            Component text,
            int x,
            int y,
            int color,
            int glowColor,
            int darkColor,
            int darkerColor,
            int baseAlpha,
            float animationPhase,
            float phaseOffset,
            float fadeAlpha
    ) {
        int fadedBaseAlpha = Mth.floor(baseAlpha * fadeAlpha);
        guiGraphics.drawString(minecraft.font, text, x, y, color + (fadedBaseAlpha << 24), false);

        float phase = animationPhase + phaseOffset;
        int alpha = Mth.floor(255.0F * fadeAlpha
                * (0.7F + Mth.abs(0.3F * Mth.sin((float) ((phase / 20.0F) % Math.TAU)))));
        int base = (alpha << 24) | glowColor;
        int dim = (base & 0xFFFFFF) | ((alpha / 3) << 24);
        int baseDark = (alpha << 24) | darkColor;
        int dimmer = (baseDark & 0xFFFFFF) | ((alpha / 4) << 24);
        int baseDarker = (alpha << 24) | darkerColor;
        int darkerDimmer = (baseDarker & 0xFFFFFF) | ((alpha / 4) << 24);
        String textValue = text.getString();
        ExtendedShaderInstance distortedShader = VERTEX_DISTORTED_TEXT_SHADER.getShaderInstance();
        LodestoneBufferWrapper buffer = distortedShader == null
                ? ADDITIVE_TEXT_BUFFER.apply(guiGraphics)
                : DISTORTED_TEXT_BUFFER.apply(guiGraphics);
        var pose = guiGraphics.pose().last().pose();
        Font font = minecraft.font;
        RenderSystem.enableBlend();
        if (distortedShader != null) {
            setTextVertexShaderUniforms(distortedShader, phaseOffset);
            font.drawInBatch(textValue, x, y, dim, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            guiGraphics.bufferSource().endBatch();
            distortedShader.setUniformDefaults();

            setTextVertexShaderUniforms(distortedShader, phaseOffset + 5000F);
            font.drawInBatch(textValue, x, y, dimmer, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            guiGraphics.bufferSource().endBatch();
            distortedShader.setUniformDefaults();

            setTextVertexShaderUniforms(distortedShader, phaseOffset + 10000F);
            font.drawInBatch(textValue, x, y, darkerDimmer, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            guiGraphics.bufferSource().endBatch();
            distortedShader.setUniformDefaults();
        } else {
            float offsetMultiplier = Mth.sin((float) ((phase / 10.0F) % Math.TAU));
            float xOffset = 2.0F * offsetMultiplier;
            float yOffset = 1.0F * offsetMultiplier;
            font.drawInBatch(textValue, x + xOffset, y, dim, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            font.drawInBatch(textValue, x - xOffset, y, dimmer, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            font.drawInBatch(textValue, x, y + yOffset, dim, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
            font.drawInBatch(textValue, x, y - yOffset, dimmer, false, pose,
                    buffer, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
        }
        RenderSystem.defaultBlendFunc();
    }

    private static void setTextVertexShaderUniforms(
            ExtendedShaderInstance shader,
            float phaseOffset
    ) {
        shader.safeGetUniform("Speed").set(TEXT_DISTORTION_SPEED);
        shader.safeGetUniform("TimeOffset").set(phaseOffset);
        shader.safeGetUniform("XFrequency").set(0.10F);
        shader.safeGetUniform("YFrequency").set(0.08F);
        shader.safeGetUniform("Amplitude").set(TEXT_DISTORTION_AMPLITUDE);
    }
}
