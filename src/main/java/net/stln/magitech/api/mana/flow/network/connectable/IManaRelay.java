package net.stln.magitech.api.mana.flow.network.connectable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.flow.network.ConnectionMode;

import java.util.Set;

public interface IManaRelay extends IManaWaypoint {
    public int getRange();

    @Override
    default Set<ConnectionMode> getConnectableModes(BlockState state) {
        return Set.of(ConnectionMode.WIRELESS);
    }

    @Override
    default Set<ConnectionMode> getNextScanModes(ConnectionMode currentMode, Direction fromSide, BlockState state) {
        return Set.of(ConnectionMode.WIRELESS);
    }
}
