package net.stln.magitech.entity.magicentity.aeltherin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.util.RenderHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class AeltherinRenderer extends GeoEntityRenderer<AeltherinEntity> {

    public AeltherinRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new AeltherinModel());
    }

    @Override
    public Color getRenderColor(AeltherinEntity animatable, float partialTick, int packedLight) {
        return Color.WHITE;
    }

    @Override
    public @Nullable RenderType getRenderType(AeltherinEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderHelper.additiveCull(texture);
    }

    @Override
    protected void applyRotations(AeltherinEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        Vec3 velocity = animatable.getDeltaMovement();

        if (!velocity.equals(Vec3.ZERO)) {
            double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

            // Yaw (水平方向の回転) → 北を基準にする
            float yaw2 = (float) (-Math.toDegrees(Math.atan2(-velocity.x, velocity.z)));

            // Pitch (上下の回転)
            float pitch = (float) -Math.toDegrees(Math.atan2(-velocity.y, horizontalSpeed));

            // 回転を適用
            poseStack.rotateAround(Axis.YP.rotationDegrees(yaw2), 0, animatable.getBbHeight() / 2, 0);
            poseStack.rotateAround(Axis.XN.rotationDegrees(pitch), 0, animatable.getBbHeight() / 2, 0);
            poseStack.translate(0.0, animatable.getBbHeight() / 2, 0.0);
        }
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }
}
