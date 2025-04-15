package net.stln.magitech.magic.mana;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.util.TickScheduler;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EntityManaTickEvent {

    @SubscribeEvent
    public static void manaTick(EntityTickEvent.Pre event) {
        ManaUtil.tick(event.getEntity());
    }
}
