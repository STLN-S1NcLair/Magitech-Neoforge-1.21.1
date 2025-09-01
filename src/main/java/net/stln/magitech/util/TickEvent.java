package net.stln.magitech.util;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.stln.magitech.Magitech;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class TickEvent {

    @SubscribeEvent
    public static void doDelayedTasksServer(ServerTickEvent.Post event) {
        TickScheduler.tick(false);
    }

    @SubscribeEvent
    public static void doDelayedTasksClient(ClientTickEvent.Post event) {
        TickScheduler.tick(true);
    }
}
