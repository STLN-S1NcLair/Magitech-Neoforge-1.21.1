package net.stln.magitech.entity.magicentity.nullixis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.util.RenderHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class NullixisRenderer extends GeoEntityRenderer<NullixisEntity> {

    public NullixisRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new NullixisModel());
    }

    @Override
    public Color getRenderColor(NullixisEntity animatable, float partialTick, int packedLight) {
        return Color.WHITE;
    }

    @Override
    public @Nullable RenderType getRenderType(NullixisEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderHelper.additiveNoCull(texture);
    }

    @Override
    protected void applyRotations(NullixisEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        poseStack.translate(0.0, animatable.getBbHeight() / 2, 0.0);
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }
}
