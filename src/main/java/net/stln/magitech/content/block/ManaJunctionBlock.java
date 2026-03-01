package net.stln.magitech.content.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.stln.magitech.core.api.mana.flow.network.connectable.IManaConnector;

import java.util.Set;

public class ManaJunctionBlock extends ManaConnectableBlock implements IManaConnector {

    protected ManaJunctionBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public Set<Direction> getConnectableDirections(BlockState state) {
        return Set.of(Direction.values());
    }
}
