package net.stln.magitech.entity.magicentity.nullixis;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.nullixis.NullixisEntity;
import software.bernie.geckolib.model.GeoModel;

public class NullixisModel extends GeoModel<NullixisEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/entity/nullixis.png");
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "geo/entity/nullixis.geo.json");
    public static final ResourceLocation ANIM = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animations/entity/nullixis.animation.json");

    @Override
    public ResourceLocation getModelResource(NullixisEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(NullixisEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(NullixisEntity animatable) {
        return ANIM;
    }
}