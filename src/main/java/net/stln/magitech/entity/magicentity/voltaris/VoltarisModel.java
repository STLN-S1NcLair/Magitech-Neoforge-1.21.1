package net.stln.magitech.entity.magicentity.voltaris;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class VoltarisModel extends GeoModel<VoltarisEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/entity/voltaris.png");
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "geo/entity/voltaris.geo.json");
    public static final ResourceLocation ANIM = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animations/entity/voltaris.animation.json");

    @Override
    public ResourceLocation getModelResource(VoltarisEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(VoltarisEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(VoltarisEntity animatable) {
        return ANIM;
    }
}