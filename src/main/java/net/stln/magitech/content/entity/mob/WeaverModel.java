package net.stln.magitech.content.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class WeaverModel extends GeoModel<WeaverEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/weaver.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/weaver.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/entity/weaver.animation.json");

    @Override
    public ResourceLocation getModelResource(WeaverEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(WeaverEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(WeaverEntity animatable) {
        return ANIM;
    }

    @Override
    public void setCustomAnimations(
            WeaverEntity animatable,
            long instanceId,
            AnimationState<WeaverEntity> animationState) {

        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData data =
                    animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotY(data.netHeadYaw() * Mth.DEG_TO_RAD);
            head.setRotX(-data.headPitch() * Mth.DEG_TO_RAD);
        }
    }
}
