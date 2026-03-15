package net.stln.magitech.effect.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import team.lodestar.lodestone.helpers.ColorHelper;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.awt.*;
import java.util.function.Function;

public class TrailRenderHelper {
    public static void renderEntityTrail(PoseStack poseStack, VFXBuilders.WorldVFXBuilder builder, TrailPointBuilder trailPointBuilder, Entity entity, Color primary, Color secondary, float scale, float alpha, float partialTicks) {
        poseStack.pushPose();
        float offx = (float) Mth.lerp(partialTicks, entity.xOld, entity.getX());
        float offy = (float) Mth.lerp(partialTicks, entity.yOld, entity.getY());
        float offz = (float) Mth.lerp(partialTicks, entity.zOld, entity.getZ());
        poseStack.translate(-offx, -offy, -offz);
        float size = 0.5f * scale;
        builder.setAlpha(alpha)
                .renderTrail(trailPointBuilder, f -> size, f -> builder.setAlpha(alpha * f).setColor(ColorHelper.colorLerp(Easing.SINE_IN, f, secondary, primary)));
        poseStack.translate(offx, offy, offz);
        poseStack.popPose();
    }

    public static void renderTrail(PoseStack poseStack, VFXBuilders.WorldVFXBuilder builder, TrailPointBuilder trailPointBuilder, Color primary, Color secondary, float scale, float alpha, float partialTicks) {
        poseStack.pushPose();
        float size = 0.5f * scale;
        builder.setAlpha(alpha)
                .renderTrail(trailPointBuilder, f -> size, f -> builder.setAlpha(alpha * f).setColor(ColorHelper.colorLerp(Easing.SINE_IN, f, secondary, primary)));
        poseStack.popPose();
    }

    public static Function<VFXBuilders.WorldVFXBuilder, VFXBuilders.WorldVFXBuilder> defaultBuilderFunc() {
        var renderType = LodestoneRenderTypes.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeTokenInit.TRAIL);
        return (builder) -> builder.setRenderType(renderType).setColor(new Color(0xFFFFFF));
    }
}
