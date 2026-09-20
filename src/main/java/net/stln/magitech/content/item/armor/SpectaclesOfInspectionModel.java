package net.stln.magitech.content.item.armor;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.GeoModel;

public class SpectaclesOfInspectionModel extends GeoModel<SpectaclesOfInspectionItem> {
    private static final ResourceLocation GEO = Magitech.id("geo/item/armor/spectacles_of_inspection.geo.json");
    private static final ResourceLocation TEXTURE = Magitech.id("textures/item/spectacles_of_insight_model.png");

    @Override
    public ResourceLocation getModelResource(SpectaclesOfInspectionItem animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(SpectaclesOfInspectionItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SpectaclesOfInspectionItem animatable) {
        return null;
    }
}
