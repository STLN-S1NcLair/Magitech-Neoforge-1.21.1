package net.stln.magitech.content.block.block_entity.renderer;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.block_entity.CrusherBlockEntity;
import net.stln.magitech.content.block.block_entity.ManaVesselBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class CrusherBlockEntityModel extends GeoModel<CrusherBlockEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/block/crusher_inside.png");
    public static final ResourceLocation GEO = Magitech.id("geo/block/crusher.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/block/crusher.animation.json");

    @Override
    public ResourceLocation getModelResource(CrusherBlockEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(CrusherBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(CrusherBlockEntity animatable) {
        return ANIM;
    }
}
