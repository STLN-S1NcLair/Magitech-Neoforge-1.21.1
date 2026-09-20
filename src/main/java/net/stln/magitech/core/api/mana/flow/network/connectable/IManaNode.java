package net.stln.magitech.core.api.mana.flow.network.connectable;

import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.network.ConnectionMode;

import java.util.Set;

/**
 * 有線・無線接続の両方を扱えるマナネットワークノードの API です。
 * API for mana-network nodes that support both wired and wireless connections.
 */
public interface IManaNode extends IManaWirelessWaypoint {

    @Override
    default Set<ConnectionMode> getConnectableModes(BlockState state) {
        return Set.of(ConnectionMode.WIRED, ConnectionMode.WIRELESS);
    }
}
