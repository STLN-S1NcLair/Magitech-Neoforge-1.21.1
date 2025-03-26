package net.stln.magitech.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.EntityInit;
import net.stln.magitech.entity.client.MagicBulletModel;
import net.stln.magitech.entity.client.MagicBulletRenderer;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MagicBulletModel.LAYER_LOCATION, MagicBulletModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.MAGIC_BULLET.get(), MagicBulletRenderer::new);
    }
}
