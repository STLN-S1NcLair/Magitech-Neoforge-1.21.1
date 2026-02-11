package net.stln.magitech.api.mana.flow.network.connectable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.api.mana.flow.network.ConnectionMode;

import java.util.Set;

// マナネットワークに接続可能なブロックとBEの基本インターフェース
// スキャン時に使用され、特定の方向と状態での接続可能な方向とモードを定義する
// BEも見るので、ブロックとBE両方で実装されることを想定
public interface IManaConnectable {
    // 接続可能な方向を取得する
    Set<Direction> getConnectableDirections(BlockState state);

    Set<ConnectionMode> getConnectableModes(BlockState state);
}
