package net.stln.magitech.api.mana.flow.network.connectable;

import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.flow.network.ConnectionMode;

import java.util.Set;

public interface IWiredEndpointManaContainer extends IManaEndpoint {

    @Override
    default Set<ConnectionMode> getConnectableModes(BlockState state) {
        return Set.of(ConnectionMode.WIRED);
    }
}
