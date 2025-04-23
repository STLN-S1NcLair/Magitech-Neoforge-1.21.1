package net.stln.magitech.magic.cooldown;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.charge.ChargeUtil;

@EventBusSubscriber(modid = Magitech.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EntityCooldownTickEvent {

    @SubscribeEvent
    public static void cooldownTick(EntityTickEvent.Pre event) {
        CooldownUtil.tick(event.getEntity());
    }
}
