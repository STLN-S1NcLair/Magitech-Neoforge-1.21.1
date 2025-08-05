package net.stln.magitech.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

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
                        .setCullState(RenderStateShard.NO_CULL) // 👈 カリング無効化
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
                        .setCullState(RenderStateShard.CULL) // 👈 カリング無効化
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(false)
        );
    }

    public static void renderFramedText(GuiGraphics guiGraphics, Font font, String text, int x, int y, Element element) {
        renderFramedText(guiGraphics, font, text, x, y, element.getSpellColor(), element.getSpellDark());
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
}
