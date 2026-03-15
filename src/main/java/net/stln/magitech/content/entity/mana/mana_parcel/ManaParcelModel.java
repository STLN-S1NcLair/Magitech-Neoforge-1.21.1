package net.stln.magitech.content.entity.mana.mana_parcel;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class ManaParcelModel extends GeoModel<ManaParcelEntity> {
    public static final ResourceLocation TEXTURE = Magitech.id("textures/entity/arcaleth.png");
    public static final ResourceLocation GEO = Magitech.id("geo/entity/arcaleth.geo.json");

    @Override
    public ResourceLocation getModelResource(ManaParcelEntity animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(ManaParcelEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ManaParcelEntity animatable) {
        return null;
    }
}
