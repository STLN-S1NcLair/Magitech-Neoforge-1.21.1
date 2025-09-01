package net.stln.magitech.magic;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.stln.magitech.Magitech;
import net.stln.magitech.magic.charge.ChargeData;
import net.stln.magitech.magic.cooldown.CooldownData;
import net.stln.magitech.magic.mana.ManaData;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class LoadCleanupEvent {

    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        ChargeData.cleanUp(player);
        CooldownData.cleanUp(player);
        ManaData.cleanUp(player);
    }
}
