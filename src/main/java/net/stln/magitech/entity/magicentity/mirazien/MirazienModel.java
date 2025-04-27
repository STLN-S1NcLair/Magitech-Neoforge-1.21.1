package net.stln.magitech.entity.magicentity.mirazien;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class MirazienModel extends GeoModel<MirazienEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/entity/mirazien.png");
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "geo/entity/mirazien.geo.json");

    @Override
    public ResourceLocation getModelResource(MirazienEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(MirazienEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(MirazienEntity animatable) {
        return null;
    }
}