package net.stln.magitech.entity.magicentity.volkarin;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class VolkarinModel extends GeoModel<VolkarinEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/volkarin.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/volkarin.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/entity/volkarin.animation.json");

    @Override
    public ResourceLocation getModelResource(VolkarinEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(VolkarinEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(VolkarinEntity animatable) {
        return ANIM;
    }
}
