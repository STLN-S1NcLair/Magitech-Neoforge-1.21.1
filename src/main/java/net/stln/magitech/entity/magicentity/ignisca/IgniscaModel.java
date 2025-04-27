package net.stln.magitech.entity.magicentity.ignisca;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.frigala.FrigalaEntity;
import software.bernie.geckolib.model.GeoModel;

public class IgniscaModel extends GeoModel<IgniscaEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/entity/ignisca.png");
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "geo/entity/ignisca.geo.json");

    @Override
    public ResourceLocation getModelResource(IgniscaEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(IgniscaEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(IgniscaEntity animatable) {
        return null;
    }
}