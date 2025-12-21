package net.stln.magitech.entity.magicentity.hydrelux;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class HydreluxModel extends GeoModel<HydreluxEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/hydrelux.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/hydrelux.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/entity/hydrelux.animation.json");

    @Override
    public ResourceLocation getModelResource(HydreluxEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(HydreluxEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(HydreluxEntity animatable) {
        return ANIM;
    }
}
