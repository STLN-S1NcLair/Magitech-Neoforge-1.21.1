package net.stln.magitech.magic;

import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.mana.ManaData;
import net.stln.magitech.util.Map2d;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class LoadCleanupEvent {

    @SubscribeEvent
    public static void chargeTick(LevelEvent.Load event) {
        ChargeData.cleanUp();
        ManaData.cleanUp();
    }
}
