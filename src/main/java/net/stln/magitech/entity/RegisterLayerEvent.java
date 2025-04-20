package net.stln.magitech.entity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.arcaleth.ArcalethModel;
import net.stln.magitech.entity.magicentity.arcaleth.ArcalethRenderer;
import net.stln.magitech.entity.magicentity.mirazien.MirazienModel;
import net.stln.magitech.entity.magicentity.mirazien.MirazienRenderer;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class RegisterLayerEvent {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ArcalethModel.LAYER_LOCATION, ArcalethModel::createBodyLayer);
        event.registerLayerDefinition(MirazienModel.LAYER_LOCATION, MirazienModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.ARCALETH_ENTITY.get(), ArcalethRenderer::new);
        event.registerEntityRenderer(EntityInit.MIRAZIEN_ENTITY.get(), MirazienRenderer::new);
    }
}
