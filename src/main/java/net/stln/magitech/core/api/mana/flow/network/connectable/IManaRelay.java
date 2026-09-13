package net.stln.magitech.core.api.mana.flow.network.connectable;

import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.network.ConnectionMode;

import java.util.Set;

/**
 * 無線接続を中継するマナネットワーク中継点の API です。
 * API for mana-network relays that provide wireless connections.
 */
public interface IManaRelay extends IManaWirelessWaypoint {

    @Override
    default Set<ConnectionMode> getConnectableModes(BlockState state) {
        return Set.of(ConnectionMode.WIRELESS);
    }
}
