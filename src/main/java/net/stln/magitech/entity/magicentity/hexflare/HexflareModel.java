package net.stln.magitech.entity.magicentity.hexflare;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class HexflareModel extends GeoModel<HexflareEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/hexflare.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/hexflare.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/entity/hexflare.animation.json");

    @Override
    public ResourceLocation getModelResource(HexflareEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(HexflareEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(HexflareEntity animatable) {
        return ANIM;
    }
}
