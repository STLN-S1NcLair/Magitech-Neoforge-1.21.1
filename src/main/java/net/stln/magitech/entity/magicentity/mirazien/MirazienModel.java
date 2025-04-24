package net.stln.magitech.entity.magicentity.mirazien;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import org.checkerframework.checker.units.qual.C;
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