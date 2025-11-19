package net.stln.magitech.item.armor;

import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class FlamglideStriderRenderer extends GeoArmorRenderer<FlamglideStriderItem> {
    public FlamglideStriderRenderer() {
        super(new DefaultedItemGeoModel<>(Magitech.id("armor/flamglide_strider")));// Using DefaultedItemGeoModel like this puts our 'location' as item/armor/example armor in the assets folders.
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
