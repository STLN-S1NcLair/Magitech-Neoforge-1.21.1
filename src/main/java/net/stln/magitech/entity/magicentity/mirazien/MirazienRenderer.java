package net.stln.magitech.entity.magicentity.mirazien;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.Magitech;
import org.jetbrains.annotations.NotNull;

public class MirazienRenderer extends EntityRenderer<MirazienEntity> {
    protected MirazienModel model;

    public MirazienRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new MirazienModel(ctx.bakeLayer(MirazienModel.LAYER_LOCATION));
    }

    @Override
    public boolean shouldRender(MirazienEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(MirazienEntity entity, float yaw, float tickDelta, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        Vec3 velocity = entity.getDeltaMovement();

        if (!velocity.equals(Vec3.ZERO)) {
            double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

            // Yaw (水平方向の回転) → 北を基準にする
            float yaw2 = (float) (180 - Math.toDegrees(Math.atan2(-velocity.x, velocity.z)));

            // Pitch (上下の回転)
            float pitch = (float) Math.toDegrees(Math.atan2(-velocity.y, horizontalSpeed));

            // 回転を適用
            poseStack.translate(0.0, 0.0, 0.0); // モデルの中心を基準に
            poseStack.mulPose(Axis.YP.rotationDegrees(yaw2));
            poseStack.mulPose(Axis.XN.rotationDegrees(pitch));
        }

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.energySwirl(getTextureLocation(entity), 0, 0));

        this.model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        poseStack.popPose();
        super.render(entity, yaw, tickDelta, poseStack, bufferSource, light);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MirazienEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/entity/mirazien/mirazien.png");
    }
}
