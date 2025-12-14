package net.stln.magitech.entity.magicentity.illusflare;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class IllusflareModel extends GeoModel<IllusflareEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/illusflare.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/illusflare.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/entity/illusflare.animation.json");

    @Override
    public ResourceLocation getModelResource(IllusflareEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(IllusflareEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(IllusflareEntity animatable) {
        return ANIM;
    }
}
