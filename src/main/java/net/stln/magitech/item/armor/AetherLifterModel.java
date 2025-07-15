package net.stln.magitech.item.armor;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.aeltherin.AeltherinEntity;
import software.bernie.geckolib.model.GeoModel;

public class AetherLifterModel extends GeoModel<AetherLifterItem> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/armor/aether_lifter.png");
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "geo/item/armor/aether_lifter.geo.json");
    public static final ResourceLocation ANIM = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "animations/armor/aether_lifter.animation.json");

    @Override
    public ResourceLocation getModelResource(AetherLifterItem animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(AetherLifterItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AetherLifterItem animatable) {
        return null;
    }
}