package net.stln.magitech.block.block_entity.renderer;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.block.block_entity.ManaVesselBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class ManaVesselBlockEntityModel extends GeoModel<ManaVesselBlockEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/block/mana_vessel_crystal.png");
    public static final ResourceLocation GEO = Magitech.id("geo/block/mana_vessel.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/armor/mana_vessel.animation.json");

    @Override
    public ResourceLocation getModelResource(ManaVesselBlockEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(ManaVesselBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ManaVesselBlockEntity animatable) {
        return null;
    }
}
