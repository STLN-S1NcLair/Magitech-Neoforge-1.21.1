package net.stln.magitech.content.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.network.connectable.IManaNode;

import java.util.Set;

public class ManaNodeBlock extends NodeBlock implements IManaNode {

    protected final int range;

    public ManaNodeBlock(Properties properties, int range) {
        super(properties);
        this.range = range;
    }

    @Override
    public Set<Direction> getConnectableDirections(BlockState state) {
        return Set.of(state.getValue(FACING).getOpposite());
    }

    @Override
    public int getRange() {
        return range;
    }
}
