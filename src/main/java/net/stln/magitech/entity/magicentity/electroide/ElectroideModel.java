package net.stln.magitech.entity.magicentity.electroide;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class ElectroideModel extends GeoModel<ElectroideEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/electroide.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/electroide.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/entity/electroide.animation.json");

    @Override
    public ResourceLocation getModelResource(ElectroideEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(ElectroideEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ElectroideEntity animatable) {
        return ANIM;
    }
}
