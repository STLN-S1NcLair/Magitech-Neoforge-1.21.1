package net.stln.magitech.api.mana.flow.network.connectable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.flow.network.ConnectionMode;

import java.util.Set;

public interface IManaNode extends IManaWirelessWaypoint {

    @Override
    default Set<ConnectionMode> getConnectableModes(BlockState state) {
        return Set.of(ConnectionMode.WIRED, ConnectionMode.WIRELESS);
    }
}
