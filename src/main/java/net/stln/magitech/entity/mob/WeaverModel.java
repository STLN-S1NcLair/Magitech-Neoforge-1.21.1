package net.stln.magitech.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

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
}
