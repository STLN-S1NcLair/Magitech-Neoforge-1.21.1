package net.stln.magitech.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderItemInFrameEvent;
import net.stln.magitech.Magitech;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class RenderItemEvent {

    @SubscribeEvent
    public static void onPartToolItemRender(RenderItemInFrameEvent event) {

    }
}
