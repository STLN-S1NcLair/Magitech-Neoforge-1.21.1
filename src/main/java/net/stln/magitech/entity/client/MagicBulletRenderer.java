package net.stln.magitech.entity.client;

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
import net.stln.magitech.entity.MagicBulletEntity;
import org.jetbrains.annotations.NotNull;

public class MagicBulletRenderer extends EntityRenderer<MagicBulletEntity> {

    public static RenderType ADDITIVE = RenderType.create("additive", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.ShaderStateShard.RENDERTYPE_GLINT_SHADER)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                    .createCompositeState(false));
    protected MagicBulletModel model;

    public MagicBulletRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new MagicBulletModel(ctx.bakeLayer(MagicBulletModel.LAYER_LOCATION));
    }

    @Override
    public boolean shouldRender(MagicBulletEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(MagicBulletEntity entity, float yaw, float tickDelta, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light) {
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
    public @NotNull ResourceLocation getTextureLocation(@NotNull MagicBulletEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/entity/magic_bullet/magic_bullet.png");
    }
}
