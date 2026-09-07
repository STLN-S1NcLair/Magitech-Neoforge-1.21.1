package net.stln.magitech.content.entity.magicentity.aetherix;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class AetherixModel extends GeoModel<AetherixEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/aetherix.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/aetherix.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/entity/aetherix.animation.json");

    @Override
    public ResourceLocation getModelResource(AetherixEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(AetherixEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AetherixEntity animatable) {
        return ANIM;
    }
}
