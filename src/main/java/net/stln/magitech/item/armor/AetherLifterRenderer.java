package net.stln.magitech.item.armor;

import net.minecraft.resources.ResourceLocation;
import net.stln.magitech.Magitech;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class AetherLifterRenderer extends GeoArmorRenderer<AetherLifterItem> {
    public AetherLifterRenderer() {
        super(new DefaultedItemGeoModel<>(Magitech.id("armor/aether_lifter")));// Using DefaultedItemGeoModel like this puts our 'location' as item/armor/example armor in the assets folders.
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
