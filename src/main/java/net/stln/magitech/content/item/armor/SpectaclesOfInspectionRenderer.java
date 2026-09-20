package net.stln.magitech.content.item.armor;

import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SpectaclesOfInspectionRenderer extends GeoArmorRenderer<SpectaclesOfInspectionItem> {
    public SpectaclesOfInspectionRenderer() {
        super(new SpectaclesOfInspectionModel());
    }

    @Override
    public GeoBone getHeadBone(GeoModel<SpectaclesOfInspectionItem> model) {
        return model.getBone("bone").orElse(null);
    }
}
