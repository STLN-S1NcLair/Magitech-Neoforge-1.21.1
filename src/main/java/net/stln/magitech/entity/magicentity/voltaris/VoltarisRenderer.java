package net.stln.magitech.entity.magicentity.voltaris;

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

public class VoltarisRenderer extends GeoEntityRenderer<VoltarisEntity> {

    public VoltarisRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new VoltarisModel());
    }

    @Override
    public Color getRenderColor(VoltarisEntity animatable, float partialTick, int packedLight) {
        return Color.WHITE;
    }

    @Override
    public @Nullable RenderType getRenderType(VoltarisEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderHelper.additiveCull(texture);
    }

    @Override
    protected void applyRotations(VoltarisEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        Vec3 velocity = animatable.getDeltaMovement();

        if (!velocity.equals(Vec3.ZERO)) {
            double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

            // Yaw (水平方向の回転) → 北を基準にする
            float yaw2 = (float) (-Math.toDegrees(Math.atan2(-velocity.x, velocity.z)));

            // Pitch (上下の回転)
            float pitch = (float) -Math.toDegrees(Math.atan2(-velocity.y, horizontalSpeed));

            // 回転を適用
            poseStack.translate(0.0, 0.0, 0.0); // モデルの中心を基準に
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw2));
            poseStack.mulPose(Axis.XN.rotationDegrees(pitch));
        }
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
    }
}
