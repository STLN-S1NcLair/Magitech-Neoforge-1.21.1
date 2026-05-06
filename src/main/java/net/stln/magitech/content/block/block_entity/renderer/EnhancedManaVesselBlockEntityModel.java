package net.stln.magitech.content.block.block_entity.renderer;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.block_entity.EnhancedManaVesselBlockEntity;
import net.stln.magitech.content.block.block_entity.ManaVesselBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class EnhancedManaVesselBlockEntityModel extends GeoModel<EnhancedManaVesselBlockEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/block/enhanced_mana_vessel_crystal.png");
    public static final ResourceLocation GEO = Magitech.id("geo/block/mana_vessel.geo.json");

    @Override
    public ResourceLocation getModelResource(EnhancedManaVesselBlockEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(EnhancedManaVesselBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(EnhancedManaVesselBlockEntity animatable) {
        return null;
    }
}
