package net.stln.magitech.entity.magicentity.tremivox;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.ignisca.IgniscaEntity;
import software.bernie.geckolib.model.GeoModel;

public class TremivoxModel extends GeoModel<TremivoxEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/entity/tremivox.png");
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "geo/entity/tremivox.geo.json");
    public static final ResourceLocation ANIM = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animations/entity/tremivox.animation.json");

    @Override
    public ResourceLocation getModelResource(TremivoxEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(TremivoxEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(TremivoxEntity animatable) {
        return ANIM;
    }
}