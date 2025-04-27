package net.stln.magitech.entity.magicentity.aeltherin;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.voltaris.VoltarisEntity;
import software.bernie.geckolib.model.GeoModel;

public class AeltherinModel extends GeoModel<AeltherinEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/entity/aeltherin.png");
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "geo/entity/aeltherin.geo.json");
    public static final ResourceLocation ANIM = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animations/entity/aeltherin.animation.json");

    @Override
    public ResourceLocation getModelResource(AeltherinEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(AeltherinEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AeltherinEntity animatable) {
        return ANIM;
    }
}