package net.stln.magitech.content.block.block_entity.renderer;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.content.block.block_entity.CompressorBlockEntity;
import net.stln.magitech.content.block.block_entity.CrusherBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class CompressorBlockEntityModel extends GeoModel<CompressorBlockEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = Magitech.id("textures/block/compressor_inside.png");
    public static final ResourceLocation GEO = Magitech.id("geo/block/compressor.geo.json");
    public static final ResourceLocation ANIM = Magitech.id("animations/block/compressor.animation.json");

    @Override
    public ResourceLocation getModelResource(CompressorBlockEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(CompressorBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(CompressorBlockEntity animatable) {
        return ANIM;
    }
}
