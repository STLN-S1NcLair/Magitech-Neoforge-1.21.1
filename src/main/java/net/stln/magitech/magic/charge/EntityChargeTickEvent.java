package net.stln.magitech.magic.charge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.stln.magitech.Magitech;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class EntityChargeTickEvent {

    @SubscribeEvent
    public static void chargeTick(EntityTickEvent.Pre event) {
        ChargeUtil.tick(event.getEntity());
    }
}
