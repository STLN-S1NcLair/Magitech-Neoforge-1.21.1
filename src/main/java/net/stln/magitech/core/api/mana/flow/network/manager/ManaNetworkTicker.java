package net.stln.magitech.core.api.mana.flow.network.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.stln.magitech.Magitech;

/**
 * ワールド tick ごとにマナネットワーク管理を更新します。
 * Updates the mana-network manager on each world tick.
 */
@EventBusSubscriber(modid = Magitech.MOD_ID)
public class ManaNetworkTicker {

    /**
     * サーバー側のマナネットワーク管理を更新します。
     * Updates the server-side mana-network manager.
     */
    @SubscribeEvent
    public static void tickNetworkManager(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (level.isClientSide() || !level.getServer().tickRateManager().runsNormally()) return;
        ManaNetworkManager.get((ServerLevel) level).tick(level);
    }
}
