package net.stln.magitech.entity.magicentity.frigala;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class FrigalaModel extends GeoModel<FrigalaEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/frigala.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/frigala.geo.json");

    @Override
    public ResourceLocation getModelResource(FrigalaEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(FrigalaEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FrigalaEntity animatable) {
        return null;
    }
}
