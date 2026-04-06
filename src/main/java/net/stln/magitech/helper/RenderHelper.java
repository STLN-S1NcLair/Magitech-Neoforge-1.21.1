package net.stln.magitech.helper;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.feature.element.Element;
import net.stln.magitech.feature.tool.ToolStats;
import net.stln.magitech.feature.tool.property.modifier.ToolPropertyModifier;
import net.stln.magitech.feature.tool.tool_type.ToolType;
import net.stln.magitech.feature.tool.upgrade.UpgradeInstance;

import java.util.List;

public class RenderHelper {
    public static RenderType additiveNoCull(ResourceLocation texture) {

        return RenderType.create("additive_nocull",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                1536,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.RENDERTYPE_ENERGY_SWIRL_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                        .setCullState(RenderStateShard.NO_CULL)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(false)
        );
    }

    public static RenderType additiveCull(ResourceLocation texture) {

        return RenderType.create("additive_nocull",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                1536,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.RENDERTYPE_ENERGY_SWIRL_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                        .setCullState(RenderStateShard.CULL)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(false)
        );
    }

    public static void renderFramedText(GuiGraphics guiGraphics, Font font, String text, int x, int y, Element element) {
        renderFramedText(guiGraphics, font, text, x, y, element.getTextColor().getRGB(), element.getDark().getRGB());
    }

    public static void renderFramedText(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color, int frameColor) {
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                guiGraphics.drawString(font, text,
                        x + (float) i / 2, y + (float) j / 2,
                        frameColor, false);
            }
        }
        guiGraphics.drawString(font, text,
                x, y,
                color, false);
    }

    public static Component getGradationGauge(int min, int max, int current, int length, int startColor, int endColor) {
        double ratio = (double) (current - min) / (max - min);
        int litLength = (int) (ratio * length);
        MutableComponent gauge = Component.empty();
        for (int i = 0; i < length; i++) {
            if (i < litLength) {
                double colorRatio = (double) i / (length - 1);
                int r = (int) ((startColor >> 16 & 0xFF) * (1 - colorRatio) + (endColor >> 16 & 0xFF) * colorRatio);
                int g = (int) ((startColor >> 8 & 0xFF) * (1 - colorRatio) + (endColor >> 8 & 0xFF) * colorRatio);
                int b = (int) ((startColor & 0xFF) * (1 - colorRatio) + (endColor & 0xFF) * colorRatio);
                int color = (r << 16) | (g << 8) | b;
                gauge.append(Component.literal("|").withStyle(style -> style.withColor(color)));
            } else {
                gauge.append(Component.literal("|").withStyle(style -> style.withColor(0x404040)));
            }
        }
        return gauge;
    }

    // TODO: 実装

    public static List<Component> getUpgradeTooltips(List<UpgradeInstance> instances, ToolType type, boolean isSpellCaster) {
        List<List<ToolPropertyModifier>> mods = instances.stream().map(UpgradeInstance::getModifiers).toList();
        List<Component> tooltipComponents = new java.util.ArrayList<>();
        return tooltipComponents;
    }
}
