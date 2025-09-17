package net.stln.magitech.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.item.tool.ToolStats;
import net.stln.magitech.item.tool.ToolType;
import net.stln.magitech.item.tool.register.ToolMaterialRegister;
import net.stln.magitech.item.tool.upgrade.UpgradeInstance;

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

    public static List<Component> getUpgradeTooltips(List<UpgradeInstance> instances, ToolType type, boolean isSpellCaster) {
        List<ToolStats> statsList = instances.stream().map(i -> ToolStats.mulWithoutElementCode(i.upgrade.getUpgradeStats(i.level), ToolMaterialRegister.getBaseStats(type))).toList();
        ToolStats finalStats = ToolStats.addWithoutElementCode(statsList);
        List<Component> tooltipComponents = new java.util.ArrayList<>();
        if (finalStats.getStats().get(ToolStats.ATK_STAT) > 0) {
        tooltipComponents.add(Component.translatable(isSpellCaster ? "attribute.magitech.spell_power" : "attribute.magitech.attack_damage").withColor(0xa0a0a0)
                .append(Component.literal(" +" + MathUtil.round(finalStats.getStats().get(ToolStats.ATK_STAT), 2)).withColor(0xFF4040)));
        }

        if (finalStats.getStats().get(ToolStats.ELM_ATK_STAT) > 0) {
            tooltipComponents.add(Component.translatable(isSpellCaster ? "attribute.magitech.elemental_spell_power" : "attribute.magitech.elemental_damage").withColor(0xa0a0a0)
                    .append(Component.literal(" +" + MathUtil.round(finalStats.getStats().get(ToolStats.ELM_ATK_STAT), 2)).withColor(0x80FF80)));
        }

            if (finalStats.getStats().get(ToolStats.SPD_STAT) > 0) {
                tooltipComponents.add(Component.translatable(isSpellCaster ? "attribute.magitech.casting_speed" : "attribute.magitech.attack_speed").withColor(0xa0a0a0)
                        .append(Component.literal(" +" + MathUtil.round(finalStats.getStats().get(ToolStats.SPD_STAT), 2)).withColor(0x40FFC0)));
            }

                if (finalStats.getStats().get(ToolStats.MIN_STAT) > 0) {
                    tooltipComponents.add(Component.translatable(isSpellCaster ? "attribute.magitech.cooldown_speed" : "attribute.magitech.mining_speed").withColor(0xa0a0a0)
                            .append(Component.literal(" +" + MathUtil.round(finalStats.getStats().get(ToolStats.MIN_STAT), 2)).withColor(0x4080C0)));
                }

                    if (finalStats.getStats().get(ToolStats.DEF_STAT) > 0) {
                        tooltipComponents.add(Component.translatable("attribute.magitech.defense").withColor(0xa0a0a0)
                                .append(Component.literal(" +" + MathUtil.round(finalStats.getStats().get(ToolStats.DEF_STAT), 2)).withColor(0xA0C0C0)));
                    }

                        if (finalStats.getStats().get(ToolStats.RNG_STAT) > 0) {
                            tooltipComponents.add(Component.translatable(isSpellCaster ? "attribute.magitech.projectile_speed" : "attribute.magitech.attack_range").withColor(0xa0a0a0)
                                    .append(Component.literal(" +" + MathUtil.round(finalStats.getStats().get(ToolStats.RNG_STAT), 2)).withColor(0x80c0FF)));
                        }

                            if (finalStats.getStats().get(ToolStats.SWP_STAT) > 0) {
                                tooltipComponents.add(Component.translatable(isSpellCaster ? "attribute.magitech.mana_efficiency" : "attribute.magitech.sweep_range").withColor(0xa0a0a0)
                                        .append(Component.literal(" +" + MathUtil.round(finalStats.getStats().get(ToolStats.SWP_STAT), 2)).withColor(0xFFFF80)));
                            }

                                if (finalStats.getStats().get(ToolStats.DUR_STAT) > 0) {
                                    tooltipComponents.add(Component.translatable("attribute.magitech.durability").withColor(0xa0a0a0)
                                            .append(Component.literal(" +" +
                                                    (Math.round(finalStats.getStats().get(ToolStats.DUR_STAT)))
                                            ).withColor(0xFFFFFF)));
                                }

        return tooltipComponents;
    }
}
