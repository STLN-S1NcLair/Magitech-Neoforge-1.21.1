package net.stln.magitech.entity.magicentity.shockvane;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class ShockvaneModel extends GeoModel<ShockvaneEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/shockvane.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/shockvane.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/entity/shockvane.animation.json");

    @Override
    public ResourceLocation getModelResource(ShockvaneEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(ShockvaneEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ShockvaneEntity animatable) {
        return ANIM;
    }
}
