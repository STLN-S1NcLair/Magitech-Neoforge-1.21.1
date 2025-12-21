package net.stln.magitech.entity.magicentity.nihilflare;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class NihilflareModel extends GeoModel<NihilflareEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/nihilflare.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/nihilflare.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/entity/nihilflare.animation.json");

    @Override
    public ResourceLocation getModelResource(NihilflareEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(NihilflareEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(NihilflareEntity animatable) {
        return ANIM;
    }
}
