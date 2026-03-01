package net.stln.magitech.vfx.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import team.lodestar.lodestone.helpers.ColorHelper;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.awt.*;

public class TrailRenderHelper {
    public static void renderEntityTrail(PoseStack poseStack, VFXBuilders.WorldVFXBuilder builder, TrailPointBuilder trailPointBuilder, Entity entity, Color primary, Color secondary, float scale, float alpha, float partialTicks) {
        poseStack.pushPose();
        float size = 0.5f * scale;
        float renderAlpha = 0.9f * alpha;
        builder.setAlpha(renderAlpha)
                .renderTrail(trailPointBuilder, f -> size, f -> builder.setAlpha(renderAlpha * f).setColor(ColorHelper.colorLerp(Easing.SINE_IN, f * 2.0F, secondary, primary)));
        poseStack.popPose();
    }

    public static void renderTrail(PoseStack poseStack, VFXBuilders.WorldVFXBuilder builder, TrailPointBuilder trailPointBuilder, Color primary, Color secondary, float scale, float alpha, float partialTicks) {
        poseStack.pushPose();
        float size = 0.5f * scale;
        float renderAlpha = 0.9f * alpha;
        builder.setAlpha(renderAlpha)
                .renderTrail(trailPointBuilder, f -> size, f -> builder.setAlpha(renderAlpha * f).setColor(ColorHelper.colorLerp(Easing.SINE_IN, f * 2.0F, secondary, primary)));
        poseStack.popPose();
    }
}
