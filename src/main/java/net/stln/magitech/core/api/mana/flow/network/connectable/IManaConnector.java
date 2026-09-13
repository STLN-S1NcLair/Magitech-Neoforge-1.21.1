package net.stln.magitech.core.api.mana.flow.network.connectable;

import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.network.ConnectionMode;

import java.util.Set;

/**
 * 有線接続を提供するマナネットワーク中継点の API です。
 * API for mana-network waypoints that provide wired connections.
 */
public interface IManaConnector extends IManaWaypoint {

    @Override
    default Set<ConnectionMode> getConnectableModes(BlockState state) {
        return Set.of(ConnectionMode.WIRED);
    }
}
