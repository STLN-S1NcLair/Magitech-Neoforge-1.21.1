package net.stln.magitech.content.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.network.connectable.IManaNode;

import java.util.Set;

public class ManaNodeBlock extends NodeBlock implements IManaNode {

    public ManaNodeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Set<Direction> getConnectableDirections(BlockState state) {
        return Set.of(state.getValue(FACING).getOpposite());
    }

    @Override
    public int getRange() {
        return 3;
    }
}
