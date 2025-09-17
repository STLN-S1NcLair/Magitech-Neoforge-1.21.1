package net.stln.magitech.entity.magicentity.arcaleth;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class ArcalethModel extends GeoModel<ArcalethEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/arcaleth.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/arcaleth.geo.json");

    @Override
    public ResourceLocation getModelResource(ArcalethEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(ArcalethEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ArcalethEntity animatable) {
        return null;
    }
}
