package net.stln.magitech.entity.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.stln.magitech.util.RenderHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.util.Color;

public class WeaverRenderer extends GeoEntityRenderer<WeaverEntity> {

    public WeaverRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new net.stln.magitech.entity.mob.WeaverModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    protected void applyRotations(WeaverEntity entity, PoseStack poseStack,
                                  float ageInTicks, float rotationYaw,
                                  float partialTick, float scale) {
        super.applyRotations(entity, poseStack, ageInTicks, rotationYaw, partialTick, scale);

        // --- 視線方向の補間値を計算 ---
        float netHeadYaw = entity.getViewYRot(partialTick);
        float bodyYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        float headYaw = netHeadYaw - bodyYaw; // ← 体に対する頭の差分回転
        float headPitch = entity.getViewXRot(partialTick);

        // --- 頭ボーンを取得 ---
        GeoBone head = this.getGeoModel().getBone("head").orElse(null);
        if (head != null) {
            head.setRotY(-headYaw * Mth.DEG_TO_RAD);   // 左右（ヨー）
            head.setRotX(-headPitch * Mth.DEG_TO_RAD); // 上下（ピッチ、符号反転が必要）
        }
    }
}
