package net.stln.magitech.item.armor;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class FlamglideStriderModel extends GeoModel<FlamglideStriderItem> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/armor/flamglide_strider.png");
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "geo/item/armor/flamglide_strider.geo.json");
    public static final ResourceLocation ANIM = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animations/armor/flamglide_strider.animation.json");

    @Override
    public ResourceLocation getModelResource(FlamglideStriderItem animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(FlamglideStriderItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FlamglideStriderItem animatable) {
        return null;
    }
}