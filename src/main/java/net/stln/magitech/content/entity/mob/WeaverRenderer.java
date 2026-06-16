package net.stln.magitech.content.entity.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class WeaverRenderer extends GeoEntityRenderer<WeaverEntity> {

    public WeaverRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new net.stln.magitech.content.entity.mob.WeaverModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

//    @Override
//    protected void applyRotations(WeaverEntity entity, PoseStack poseStack,
//                                  float ageInTicks, float rotationYaw,
//                                  float partialTick, float scale) {
//        super.applyRotations(entity, poseStack, ageInTicks, rotationYaw, partialTick, scale);
//
//        // --- 頭ボーンを取得 ---
//        GeoBone head = this.getGeoModel().getBone("head").orElse(null);
//        if (head != null) {
//            EntityModelData entityData =
//                    animatable.getData(DataTickets.ENTITY_MODEL_DATA);
//
//            head.setRotY(head.getRotY() + entityData.netHeadYaw() * Mth.DEG_TO_RAD);
//            head.setRotX(head.getRotX() + entityData.headPitch() * Mth.DEG_TO_RAD);
//        }
//    }
}
