package net.stln.magitech.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.stln.magitech.core.api.mana.flow.network.connectable.IManaRelay;
import net.stln.magitech.helper.VoxelShapeHelper;

import java.util.Set;

public class ManaRelayBlock extends NodeBlock implements IManaRelay {

    protected final int range;

    public static final VoxelShape SHAPE_UP = Shapes.or(
            Block.box(4, 3, 4, 12, 11, 12),
            Block.box(2, 0, 2, 14, 3, 14)
    );
    public static final VoxelShape SHAPE_DOWN = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.DOWN);
    public static final VoxelShape SHAPE_NORTH = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.NORTH);
    public static final VoxelShape SHAPE_SOUTH = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.SOUTH);
    public static final VoxelShape SHAPE_EAST = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.EAST);
    public static final VoxelShape SHAPE_WEST = VoxelShapeHelper.rotateShape(SHAPE_UP, Direction.UP, Direction.WEST);

    public ManaRelayBlock(Properties properties, int range) {
        super(properties);
        this.range = range;
    }

    @Override
    protected VoxelShape getShape(BlockState p_154346_, BlockGetter p_154347_, BlockPos p_154348_, CollisionContext p_154349_) {
        return switch (p_154346_.getValue(FACING)) {
            case UP -> SHAPE_UP;
            case DOWN -> SHAPE_DOWN;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
        };
    }

    @Override
    public Set<Direction> getConnectableDirections(BlockState state) {
        return Set.of();
    }

    @Override
    public int getRange() {
        return 4;
    }
}
