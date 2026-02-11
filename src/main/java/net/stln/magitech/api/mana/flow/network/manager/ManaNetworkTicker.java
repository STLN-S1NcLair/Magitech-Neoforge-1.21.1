package net.stln.magitech.api.mana.flow.network.manager;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.stln.magitech.Magitech;

@EventBusSubscriber(modid = Magitech.MOD_ID)
public class ManaNetworkTicker {

    @SubscribeEvent
    public static void tickNetworkManager(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;
        ManaNetworkManager.get((ServerLevel) level).tick(level);
    }
}
