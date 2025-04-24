package net.stln.magitech.entity.magicentity.frigala;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.mirazien.MirazienEntity;
import software.bernie.geckolib.model.GeoModel;

public class FrigalaModel extends GeoModel<FrigalaEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "textures/entity/frigala.png");
    public static final ResourceLocation GEO = ResourceLocation.fromNamespaceAndPath(Magitech.MOD_ID, "geo/entity/frigala.geo.json");

    @Override
    public ResourceLocation getModelResource(FrigalaEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(FrigalaEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FrigalaEntity animatable) {
        return null;
    }
}