package net.stln.magitech.core.api.mana.flow.network.connectable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.network.ConnectionMode;

import java.util.Set;

// マナネットワークに接続可能なブロックとBEの基本インターフェース / Base interface for blocks and block entities that can connect to a mana network
// スキャン時に使用され、特定の方向と状態での接続可能な方向とモードを定義する / Used during scans to define connectable directions and modes for a state
// BEも見るので、ブロックとBE両方で実装されることを想定 / Implemented by both blocks and block entities because both are inspected
/**
 * マナネットワークへ接続可能なブロックまたはブロックエンティティの基本 API です。
 * Base API for blocks or block entities that can connect to a mana network.
 */
public interface IManaConnectable {
    /**
     * 指定状態で接続可能な面を返します。
     * Returns the faces that can connect in the specified state.
     */
    Set<Direction> getConnectableDirections(BlockState state);

    /**
     * 指定状態で利用可能な接続方式を返します。
     * Returns the connection modes available in the specified state.
     */
    Set<ConnectionMode> getConnectableModes(BlockState state);
}
