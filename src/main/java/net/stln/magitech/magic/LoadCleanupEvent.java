package net.stln.magitech.magic;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.cooldown.CooldownData;
import net.stln.magitech.magic.mana.ManaData;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class LoadCleanupEvent {

    @SubscribeEvent
    public static void chargeTick(LevelEvent.Load event) {
        ChargeData.cleanUp();
        CooldownData.cleanUp();
        ManaData.cleanUp();
    }
}
