package net.stln.magitech.entity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.entity.magicentity.arcaleth.ArcalethModel;
import net.stln.magitech.entity.magicentity.arcaleth.ArcalethRenderer;
import net.stln.magitech.entity.magicentity.frigala.FrigalaModel;
import net.stln.magitech.entity.magicentity.frigala.FrigalaRenderer;
import net.stln.magitech.entity.magicentity.mirazien.MirazienModel;
import net.stln.magitech.entity.magicentity.mirazien.MirazienRenderer;
import net.stln.magitech.entity.magicentity.voltaris.VoltarisRenderer;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class RegisterLayerEvent {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.ARCALETH_ENTITY.get(), ArcalethRenderer::new);
        event.registerEntityRenderer(EntityInit.MIRAZIEN_ENTITY.get(), MirazienRenderer::new);
        event.registerEntityRenderer(EntityInit.FRIGALA_ENTITY.get(), FrigalaRenderer::new);
        event.registerEntityRenderer(EntityInit.VOLTARIS_ENTITY.get(), VoltarisRenderer::new);
    }
}
