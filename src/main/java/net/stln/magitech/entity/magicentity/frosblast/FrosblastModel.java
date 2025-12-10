package net.stln.magitech.entity.magicentity.frosblast;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class FrosblastModel extends GeoModel<FrosblastEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/frosblast.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/frosblast.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/entity/frosblast.animation.json");

    @Override
    public ResourceLocation getModelResource(FrosblastEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(FrosblastEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FrosblastEntity animatable) {
        return ANIM;
    }
}
