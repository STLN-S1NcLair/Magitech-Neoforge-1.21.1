package net.stln.magitech.core.api.mana.flow.network.connectable;

import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.network.ConnectionMode;

import java.util.Set;

/**
 * 有線ネットワーク終端として接続できるマナ容器の API です。
 * API for mana containers that can connect as wired network endpoints.
 */
public interface IWiredEndpointManaContainer extends IManaEndpoint {

    @Override
    default Set<ConnectionMode> getConnectableModes(BlockState state) {
        return Set.of(ConnectionMode.WIRED);
    }
}
